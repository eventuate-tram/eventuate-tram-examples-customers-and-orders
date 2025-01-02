package io.eventuate.examples.tram.ordersandcustomers.orders.eventhandlers;

import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.eventuate.tram.spring.optimisticlocking.OptimisticLockingDecoratorConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAutoConfiguration
@Import({OptimisticLockingDecoratorConfiguration.class})
public class OrderEventHandlersConfiguration {

  @Bean
  public CustomerEventConsumer orderEventConsumer() {
    return new CustomerEventConsumer();
  }

  @Bean
  public DomainEventDispatcher domainEventDispatcher(CustomerEventConsumer customerEventConsumer, DomainEventDispatcherFactory domainEventDispatcherFactory) {
    return domainEventDispatcherFactory.make("customerServiceEvents", customerEventConsumer.domainEventHandlers());
  }

}
