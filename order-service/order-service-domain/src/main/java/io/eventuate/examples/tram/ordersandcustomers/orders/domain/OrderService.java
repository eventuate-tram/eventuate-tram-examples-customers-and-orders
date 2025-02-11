package io.eventuate.examples.tram.ordersandcustomers.orders.domain;

import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.publisher.ResultWithEvents;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Collections.singletonList;

public class OrderService {

  private final DomainEventPublisher domainEventPublisher;
  private final OrderRepository orderRepository;


  public OrderService(DomainEventPublisher domainEventPublisher, OrderRepository orderRepository) {
    this.domainEventPublisher = domainEventPublisher;
    this.orderRepository = orderRepository;
  }

  @Transactional
  public Order createOrder(OrderDetails orderDetails) {
    ResultWithEvents<Order> orderWithEvents = Order.createOrder(orderDetails);
    Order order = orderWithEvents.result;
    orderRepository.save(order);
    domainEventPublisher.publish(Order.class, order.getId(), orderWithEvents.events);
    return order;
  }

  public void approveOrder(Long orderId) {
    Order order = orderRepository
            .findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("order with id %s not found".formatted(orderId)));
    order.noteCreditReserved();
    domainEventPublisher.publish(Order.class,
            orderId, singletonList(new OrderApprovedEvent(order.getOrderDetails())));
  }

  public void rejectOrder(Long orderId, RejectionReason rejectionReason) {
    Order order = orderRepository
            .findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("order with id %s not found".formatted(orderId)));
    order.noteCreditReservationFailed(rejectionReason);
    domainEventPublisher.publish(Order.class,
            orderId, singletonList(new OrderRejectedEvent(order.getOrderDetails())));
  }

  @Transactional
  public Order cancelOrder(Long orderId) {
    Order order = orderRepository
            .findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("order with id %s not found".formatted(orderId)));
    order.cancel();
    domainEventPublisher.publish(Order.class,
            orderId, singletonList(new OrderCancelledEvent(order.getOrderDetails())));
    return order;
  }
}
