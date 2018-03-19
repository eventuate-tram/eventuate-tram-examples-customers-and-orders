package io.eventuate.examples.tram.ordersandcustomers.orders.service;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderCreatedEvent;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderDetails;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.Order;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.OrderRepository;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static java.util.Collections.singletonList;

public class OrderService {

  @Autowired
  private DomainEventPublisher domainEventPublisher;

  @Autowired
  private OrderRepository orderRepository;

  @Transactional
  public Order createOrder(OrderDetails orderDetails) {
    Order order = Order.createOrder(orderDetails);
    orderRepository.save(order);
    domainEventPublisher.publish(Order.class,
            order.getId(),
            singletonList(new OrderCreatedEvent(order.getId(), orderDetails)));
    return order;
  }
}
