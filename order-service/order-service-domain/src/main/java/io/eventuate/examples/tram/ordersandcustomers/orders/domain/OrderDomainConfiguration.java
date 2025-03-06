package io.eventuate.examples.tram.ordersandcustomers.orders.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderDomainConfiguration {

  @Bean
  public OrderService orderService(OrderRepository orderRepository, OrderEventPublisher orderEventPublisher) {
    return new OrderService(orderRepository, orderEventPublisher);
  }

}
