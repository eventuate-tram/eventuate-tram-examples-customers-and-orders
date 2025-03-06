package io.eventuate.examples.tram.ordersandcustomers.orders.eventhandlers;

import io.eventuate.examples.tram.ordersandcustomers.orders.domain.OrderService;
import io.eventuate.tram.spring.flyway.EventuateTramFlywayMigrationConfiguration;
import io.eventuate.tram.spring.optimisticlocking.OptimisticLockingDecoratorConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@EnableAutoConfiguration
@Import({OptimisticLockingDecoratorConfiguration.class, EventuateTramFlywayMigrationConfiguration.class})
public class OrderEventHandlersConfiguration {

  @Bean
  public CustomerEventConsumer orderEventConsumer(OrderService orderService) {
    return new CustomerEventConsumer(orderService);
  }


}
