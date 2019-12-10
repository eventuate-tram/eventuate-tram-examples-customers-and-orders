package io.eventuate.examples.tram.ordersandcustomers.jmeter;

import io.eventuate.examples.tram.ordersandcustomers.commonswagger.CommonSwaggerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({CommonSwaggerConfiguration.class})
public class JMeterServiceMain {
  public static void main(String[] args) {
    SpringApplication.run(JMeterServiceMain.class, args);
  }
}
