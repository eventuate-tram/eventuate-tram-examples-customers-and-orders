package io.eventuate.examples.tram.ordersandcustomers.jmeter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class TestPlanController {

  @Value("${jmeter.service.results.folder}")
  private String resultsFolder;

  @Value("${jmeter.service.testplan.path}")
  private String testPlanPath;

  @Value("${jmeter.path}")
  private String jmeterPath;

  @RequestMapping(value = "/testplan", method = RequestMethod.POST)
  public synchronized String runTestPlan(@RequestParam("loops") int loops,
                            @RequestParam("threads") int threads,
                            @RequestParam("rampTime") int rampTime,
                            @RequestParam("domain") String domain,
                            @RequestParam("port") int port,
                            HttpServletRequest request) throws IOException, InterruptedException {

    String testPlanId = UUID.randomUUID().toString();

    configureTestPlan(loops, threads, rampTime, domain, port);

    runTestPlan(testPlanId);

    return String.format("%s/%s/index.html", request.getRequestURL(), testPlanId);
  }

  @RequestMapping(value = "/testplan/**", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
  public @ResponseBody InputStreamResource getFile(HttpServletRequest request) throws IOException {
      return new InputStreamResource(new FileInputStream(getReportFilePath(request.getRequestURI())));
  }

  private void runTestPlan(String testPlanId) throws IOException, InterruptedException {
    File testPlanReportFolder = new File(String.format("%s/%s", resultsFolder, testPlanId));
    testPlanReportFolder.mkdir();


    ProcessBuilder processBuilder = new ProcessBuilder();
    processBuilder.command(jmeterPath, "-n", "-t", testPlanPath, "-l", getTestResultPath(testPlanId));
    processBuilder.start().waitFor();

    processBuilder = new ProcessBuilder();
    processBuilder.command(jmeterPath, "-g", getTestResultPath(testPlanId), "-o", testPlanReportFolder.getAbsolutePath());
    processBuilder.start().waitFor();
  }

  private void configureTestPlan(int loops, int threads, int rampTime, String domain, int port) throws IOException {
    String testPlanContent;
    try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(testPlanPath)))) {
      testPlanContent = bufferedReader.lines().collect(Collectors.joining("\n"));
    }

    testPlanContent = testPlanContent.replaceAll("<stringProp name=\"LoopController.loops\">(.*)</stringProp>",
            String.format("<stringProp name=\"LoopController.loops\">%s</stringProp>", loops));

    testPlanContent = testPlanContent.replaceAll("<stringProp name=\"ThreadGroup.num_threads\">(.*)</stringProp>",
            String.format("<stringProp name=\"ThreadGroup.num_threads\">%s</stringProp>", threads));

    testPlanContent = testPlanContent.replaceAll("<stringProp name=\"ThreadGroup.ramp_time\">(.*)</stringProp>",
            String.format("<stringProp name=\"ThreadGroup.ramp_time\">%s</stringProp>", rampTime));

    testPlanContent = testPlanContent.replaceAll("<stringProp name=\"HTTPSampler.domain\">(.*)</stringProp>",
            String.format("<stringProp name=\"HTTPSampler.domain\">%s</stringProp>", domain));

    testPlanContent = testPlanContent.replaceAll("<stringProp name=\"HTTPSampler.port\">(.*)</stringProp>",
            String.format("<stringProp name=\"HTTPSampler.port\">%s</stringProp>", port));

    try(BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(testPlanPath)))) {
      bufferedWriter.append(testPlanContent);
      bufferedWriter.flush();
    }
  }

  private String getTestResultPath(String testPlanId) {
    return String.format("%s/%s.jtl", resultsFolder, testPlanId);
  }

  private String getReportFilePath(String uri) {
    return String.format("%s/%s", resultsFolder, uri.replace("/testplan/", ""));
  }
}
