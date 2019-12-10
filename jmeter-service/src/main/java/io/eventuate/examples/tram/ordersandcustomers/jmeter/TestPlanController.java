package io.eventuate.examples.tram.ordersandcustomers.jmeter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
public class TestPlanController {

  @Value("${jmeter.service.results.folder}")
  private String resultsFolder;

  @Value("${jmeter.service.testplan.folder}")
  private String testPlanFolder;

  @Value("${jmeter.path}")
  private String jmeterPath;

  @RequestMapping(value = "/testplan", method = RequestMethod.POST)
  public String runTestPlan(@RequestParam("testPlan") MultipartFile testPlan) throws IOException {
    testPlan.transferTo(new File(getTestPlanPath()));

    String testPlanId = UUID.randomUUID().toString();

    ProcessBuilder processBuilder = new ProcessBuilder();

    processBuilder.command(jmeterPath, "-n", "-t", getTestPlanPath(), "-l", getTestResultPath(testPlanId));

    processBuilder.start();

    return testPlanId;
  }

  @RequestMapping(value = "/testplan/{id}", method = RequestMethod.GET)
  public ResponseEntity<Resource> getTestPlan(@PathVariable("id") String testPlanId) throws IOException {
    File testPlanResult = new File(getTestResultPath(testPlanId));

    if (testPlanResult.exists()) {
      InputStreamResource resource = new InputStreamResource(new FileInputStream(testPlanResult));

      return ResponseEntity.ok()
              .contentLength(testPlanResult.length())
              .contentType(MediaType.parseMediaType("application/octet-stream"))
              .body(resource);
    }

    throw new RuntimeException("Test result is not found");
  }

  private String getTestResultPath(String testPlanId) {
    return String.format("%s/%s.jtl", resultsFolder, testPlanId);
  }

  private String getTestPlanPath() {
    return String.format("%s/testplan.jmx", testPlanFolder);
  }
}
