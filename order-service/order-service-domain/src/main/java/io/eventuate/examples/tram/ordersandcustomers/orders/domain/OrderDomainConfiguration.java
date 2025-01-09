package io.eventuate.examples.tram.ordersandcustomers.orders.domain;

import io.eventuate.tram.events.publisher.DomainEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderDomainConfiguration {

  @Bean
  public OrderService orderService(DomainEventPublisher domainEventPublisher, OrderRepository orderRepository) {
    return new OrderService(domainEventPublisher, orderRepository);
  }

}
