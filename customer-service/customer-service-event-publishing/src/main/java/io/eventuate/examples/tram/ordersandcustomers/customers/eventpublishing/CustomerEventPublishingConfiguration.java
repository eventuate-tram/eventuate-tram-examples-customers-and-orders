package io.eventuate.examples.tram.ordersandcustomers.customers.eventpublishing;

import io.eventuate.examples.tram.ordersandcustomers.customers.domain.CustomerEventPublisher;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerEventPublishingConfiguration {

  @Bean
  public CustomerEventPublisher customerEventPublisher(DomainEventPublisher domainEventPublisher) {
    return new CustomerEventPublisherImpl(domainEventPublisher);
  }
}
