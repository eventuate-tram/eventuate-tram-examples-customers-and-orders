package io.eventuate.examples.tram.ordersandcustomers.orders.domain;

import io.eventuate.tram.events.publisher.ResultWithTypedEvents;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class OrderService {

  private final OrderRepository orderRepository;
  private final OrderEventPublisher orderEventPublisher;


  public OrderService(OrderRepository orderRepository, OrderEventPublisher orderEventPublisher) {
    this.orderRepository = orderRepository;
    this.orderEventPublisher = orderEventPublisher;
  }

  @Transactional
  public Order createOrder(OrderDetails orderDetails) {
    ResultWithTypedEvents<Order, OrderEvent> orderWithEvents = Order.createOrder(orderDetails);
    Order order = orderWithEvents.getResult();
    orderRepository.save(order);
    orderEventPublisher.publish(order, orderWithEvents.getEvents());
    return order;
  }

  public void approveOrder(Long orderId) {
    Order order = orderRepository
            .findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("order with id %s not found".formatted(orderId)));
    order.noteCreditReserved();
    orderEventPublisher.publish(order, new OrderApprovedEvent(order.getOrderDetails()));
  }

  public void rejectOrder(Long orderId, RejectionReason rejectionReason) {
    Order order = orderRepository
            .findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("order with id %s not found".formatted(orderId)));
    order.noteCreditReservationFailed(rejectionReason);
    orderEventPublisher.publish(order, new OrderRejectedEvent(order.getOrderDetails()));
  }

  @Transactional
  public Order cancelOrder(Long orderId) {
    Order order = orderRepository
            .findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("order with id %s not found".formatted(orderId)));
    order.cancel();
    orderEventPublisher.publish(order, new OrderCancelledEvent(order.getOrderDetails()));
    return order;
  }

  public Optional<Order> findById(long orderId) {
    return orderRepository.findById(orderId);
  }

  public Iterable<Order> findAll() {
    return orderRepository.findAll();
  }
}
