package io.eventuate.examples.tram.ordersandcustomers.customers.eventhandlers;

import io.eventuate.examples.tram.ordersandcustomers.customers.domain.CustomerService;
import io.eventuate.tram.spring.flyway.EventuateTramFlywayMigrationConfiguration;
import io.eventuate.tram.spring.optimisticlocking.OptimisticLockingDecoratorConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({OptimisticLockingDecoratorConfiguration.class, EventuateTramFlywayMigrationConfiguration.class})
@EnableAutoConfiguration
public class CustomerServiceEventHandlerConfiguration {

  @Bean
  public OrderEventConsumer orderEventConsumer(CustomerService customerService) {
    return new OrderEventConsumer(customerService);
  }

}
