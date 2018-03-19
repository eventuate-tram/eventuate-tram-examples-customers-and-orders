package io.eventuate.examples.tram.ordersandcustomers.orders;

import io.eventuate.examples.tram.ordersandcustomers.orders.web.OrderWebConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({OrderWebConfiguration.class, OrderConfiguration.class})
public class OrderServiceMain {
  public static void main(String[] args) {
    SpringApplication.run(OrderServiceMain.class, args);
  }
}
