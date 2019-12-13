package io.eventuate.examples.tram.ordersandcustomers.jmeter;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.UUID;

@RestController
public class TestPlanController {

  @Value("${jmeter.service.results.folder}")
  private String resultsFolder;

  @Value("${jmeter.service.testplan.path}")
  private String testPlanPath;

  @Value("${jmeter.path}")
  private String jmeterPath;

  private TestPlanConfigurator testPlanConfigurator = new TestPlanConfigurator();

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
    processBuilder.command(jmeterPath, "-n", "-t", testPlanPath, "-l", getTestResultPath(testPlanId), "-e", "-o", testPlanReportFolder.getAbsolutePath());
    processBuilder.inheritIO();
    processBuilder.start();
  }

  private void configureTestPlan(int loops, int threads, int rampTime, String domain, int port) throws IOException {
    File testPlanFile = new File(testPlanPath);
    String testPlanContent = FileUtils.readFileToString(testPlanFile, "UTF-8");
    testPlanContent = testPlanConfigurator.configure(testPlanContent, loops, threads, rampTime, domain, port);
    FileUtils.writeStringToFile(testPlanFile, testPlanContent, "UTF-8");
  }

  private String getTestResultPath(String testPlanId) {
    return String.format("%s/%s.jtl", resultsFolder, testPlanId);
  }

  private String getReportFilePath(String uri) {
    return String.format("%s/%s", resultsFolder, uri.replace("/testplan/", ""));
  }
}
