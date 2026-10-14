package io.eventuate.examples.tram.customersandorders.endtoendtests;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CustomersAndOrdersEndToEndTestConfiguration {

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

}
