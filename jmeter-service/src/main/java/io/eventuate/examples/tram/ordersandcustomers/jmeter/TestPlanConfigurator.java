package io.eventuate.examples.tram.ordersandcustomers.jmeter;


import java.util.Arrays;
import java.util.List;

public class TestPlanConfigurator {

  private static final String PROPERTY_TEMPLATE = "<stringProp name=\"%s\">%s</stringProp>";

  private static final String LOOPS_PROPERTY = "LoopController.loops";
  private static final String THREADS_PROPERTY = "ThreadGroup.num_threads";
  private static final String RAMP_TIME_PROPERTY = "ThreadGroup.ramp_time";
  private static final String DOMAIN_PROPERTY = "HTTPSampler.domain";
  private static final String PORT_PROPERTY = "HTTPSampler.port";

  private static final List<String> PROPERTIES = Arrays.asList(LOOPS_PROPERTY, THREADS_PROPERTY, RAMP_TIME_PROPERTY, DOMAIN_PROPERTY, PORT_PROPERTY);

  public String configure(String testPlan, int loops, int threads, int rampTime, String domain, int port) {
    List<Object> propertyValues = Arrays.asList(loops, threads, rampTime, domain, port);

    for (int i = 0; i < PROPERTIES.size(); i++) {
      String property = PROPERTIES.get(i);
      testPlan = testPlan.replaceAll(String.format(PROPERTY_TEMPLATE, property, "(.*)"), String.format(PROPERTY_TEMPLATE, property, propertyValues.get(i)));
    }

    return testPlan;
  }
}
