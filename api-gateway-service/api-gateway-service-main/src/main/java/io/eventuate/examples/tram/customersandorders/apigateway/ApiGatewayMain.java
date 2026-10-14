package io.eventuate.examples.tram.customersandorders.apigateway;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.tools.agent.ReactorDebugAgent;

@SpringBootApplication()
public class ApiGatewayMain {

  public static void main(String[] args) {
    ReactorDebugAgent.init();
    SpringApplication.run(ApiGatewayMain.class, args);
  }
}

