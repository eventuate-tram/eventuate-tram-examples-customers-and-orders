package io.eventuate.examples.tram.ordersandcustomers.jmeter;


import java.util.Arrays;
import java.util.List;

public class TestPlanConfigurator {

  private static final String LOOPS_TEMPLATE = "<stringProp name=\"LoopController.loops\">%s</stringProp>";
  private static final String THREADS_TEMPLATE = "<stringProp name=\"ThreadGroup.num_threads\">%s</stringProp>";
  private static final String RAMP_TIME_TEMPLATE = "<stringProp name=\"ThreadGroup.ramp_time\">%s</stringProp>";
  private static final String DOMAIN_TEMPLATE = "<stringProp name=\"HTTPSampler.domain\">%s</stringProp>";
  private static final String PORT_TEMPLATE = "<stringProp name=\"HTTPSampler.port\">%s</stringProp>";

  private static final List<String> TEMPLATES = Arrays.asList(LOOPS_TEMPLATE, THREADS_TEMPLATE, RAMP_TIME_TEMPLATE, DOMAIN_TEMPLATE, PORT_TEMPLATE);

  public String configure(String testPlan, int loops, int threads, int rampTime, String domain, int port) {
    List<Object> templateParameters = Arrays.asList(loops, threads, rampTime, domain, port);

    for (int i = 0; i < TEMPLATES.size(); i++) {
      String template = TEMPLATES.get(i);
      testPlan = testPlan.replaceAll(String.format(template, "(.*)"), String.format(template, templateParameters.get(i)));
    }

    return testPlan;
  }
}
