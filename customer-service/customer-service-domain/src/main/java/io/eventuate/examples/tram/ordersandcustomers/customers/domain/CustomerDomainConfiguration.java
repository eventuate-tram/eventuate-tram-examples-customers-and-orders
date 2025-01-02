package io.eventuate.examples.tram.ordersandcustomers.customers.domain;

import io.eventuate.tram.events.publisher.DomainEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerDomainConfiguration {

  @Bean
  public CustomerService customerService(CustomerRepository customerRepository, DomainEventPublisher domainEventPublisher) {
    return new CustomerService(customerRepository, domainEventPublisher);
  }

}
