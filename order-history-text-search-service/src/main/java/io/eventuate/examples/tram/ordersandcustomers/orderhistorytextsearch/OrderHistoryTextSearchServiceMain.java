package io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch;

import io.eventuate.examples.tram.ordersandcustomers.commonswagger.CommonSwaggerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.elasticsearch.ElasticSearchRestHealthContributorAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = {
        ElasticSearchRestHealthContributorAutoConfiguration.class,
        DataSourceAutoConfiguration.class
})
@Import({OrderHistoryTestSearchConfiguration.class, CommonSwaggerConfiguration.class})
public class OrderHistoryTextSearchServiceMain {
  public static void main(String[] args) {
    SpringApplication.run(OrderHistoryTextSearchServiceMain.class, args);
  }
}
