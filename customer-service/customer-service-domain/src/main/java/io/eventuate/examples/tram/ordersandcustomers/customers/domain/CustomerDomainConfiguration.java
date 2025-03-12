package io.eventuate.examples.tram.ordersandcustomers.customers.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerDomainConfiguration {

  @Bean
  public CustomerService customerService(CustomerRepository customerRepository, CustomerEventPublisher customerEventPublisher) {
    return new CustomerService(customerRepository, customerEventPublisher);
  }

}
