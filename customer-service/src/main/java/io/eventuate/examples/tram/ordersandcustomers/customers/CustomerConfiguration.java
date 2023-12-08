package io.eventuate.examples.tram.ordersandcustomers.customers;

import io.eventuate.examples.tram.ordersandcustomers.customers.domain.CustomerRepository;
import io.eventuate.examples.tram.ordersandcustomers.customers.service.CustomerService;
import io.eventuate.examples.tram.ordersandcustomers.customers.service.OrderEventConsumer;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.eventuate.tram.spring.optimisticlocking.OptimisticLockingDecoratorConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@Import({OptimisticLockingDecoratorConfiguration.class})
@EnableJpaRepositories
@EnableAutoConfiguration
public class CustomerConfiguration {

  @Bean
  public OrderEventConsumer orderEventConsumer(CustomerService customerService) {
    return new OrderEventConsumer(customerService);
  }

  @Bean
  public DomainEventDispatcher domainEventDispatcher(OrderEventConsumer orderEventConsumer, DomainEventDispatcherFactory domainEventDispatcherFactory) {
    return domainEventDispatcherFactory.make("orderServiceEvents", orderEventConsumer.domainEventHandlers());
  }

  @Bean
  public CustomerService customerService(CustomerRepository customerRepository, DomainEventPublisher domainEventPublisher) {
    return new CustomerService(customerRepository, domainEventPublisher);
  }

}
