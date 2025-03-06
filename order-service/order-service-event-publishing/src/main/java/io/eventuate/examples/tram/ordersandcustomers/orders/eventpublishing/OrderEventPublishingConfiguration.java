package io.eventuate.examples.tram.ordersandcustomers.orders.eventpublishing;

import io.eventuate.examples.tram.ordersandcustomers.orders.domain.OrderEventPublisher;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderEventPublishingConfiguration {

  @Bean
  public OrderEventPublisher orderEventPublisher(DomainEventPublisher domainEventPublisher) {
    return new OrderEventPublisherImpl(domainEventPublisher);
  }
}
