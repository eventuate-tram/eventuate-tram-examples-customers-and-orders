package io.eventuate.examples.tram.ordersandcustomers.commonswagger;

import io.eventuate.util.spring.swagger.EventuateSwaggerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class CommonSwaggerConfiguration {
  @Bean
  public EventuateSwaggerConfig eventuateSwaggerConfig() {
    return () -> "io.eventuate.examples.tram.ordersandcustomers";
  }
}
