package io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class
})
@Import({OrderHistoryTestSearchConfiguration.class})
public class OrderHistoryTextSearchServiceMain {
  public static void main(String[] args) {
    SpringApplication.run(OrderHistoryTextSearchServiceMain.class, args);
  }
}
