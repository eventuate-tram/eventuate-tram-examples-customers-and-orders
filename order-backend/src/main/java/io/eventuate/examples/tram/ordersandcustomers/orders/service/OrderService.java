package io.eventuate.examples.tram.ordersandcustomers.orders.service;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.*;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.Order;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.OrderRepository;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.publisher.ResultWithEvents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Collections.singletonList;

@Transactional
public class OrderService {

  @Autowired
  private DomainEventPublisher domainEventPublisher;

  @Autowired
  private OrderRepository orderRepository;

  public Order createOrder(OrderDetailsDTO orderDetails) {
    ResultWithEvents<Order> orderWithEvents = Order.createOrder(orderDetails);
    Order order = orderWithEvents.result;
    orderRepository.save(order);
    domainEventPublisher.publish(Order.class, order.getId(), orderWithEvents.events);
    return order;
  }

  public void approveOrder(Long orderId) {
    Order order = orderRepository
            .findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException(String.format("order with id %s not found", orderId)));
    order.noteCreditReserved();
    OrderDetailsDTO orderDetails = new OrderDetailsDTO(order.getOrderDetails().getCustomerId(), new MoneyDTO(order.getOrderDetails().getOrderTotal().getAmount()));
    domainEventPublisher.publish(Order.class,
            orderId, singletonList(new OrderApprovedEvent(orderDetails)));
  }

  public void rejectOrder(Long orderId) {
    Order order = orderRepository
            .findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException(String.format("order with id %s not found", orderId)));
    order.noteCreditReservationFailed();
    OrderDetailsDTO orderDetails = new OrderDetailsDTO(order.getOrderDetails().getCustomerId(), new MoneyDTO(order.getOrderDetails().getOrderTotal().getAmount()));
    domainEventPublisher.publish(Order.class,
            orderId, singletonList(new OrderRejectedEvent(orderDetails)));
  }
}
