package io.eventuate.examples.tram.ordersandcustomers.orders.domain;


import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderCreatedEvent;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderDetails;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderState;
import io.eventuate.tram.events.ResultWithEvents;

import javax.persistence.*;

import static java.util.Collections.singletonList;

@Entity
@Table(name="orders")
@Access(AccessType.FIELD)
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private OrderState state;

  @Embedded
  private OrderDetails orderDetails;

  public Order() {
  }

  public Order(OrderDetails orderDetails) {
    this.orderDetails = orderDetails;
    this.state = OrderState.PENDING;
  }

  public static ResultWithEvents<Order> createOrder(OrderDetails orderDetails) {
    Order order = new Order(orderDetails);
    OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(orderDetails);
    return new ResultWithEvents<>(order, singletonList(orderCreatedEvent));
  }

  public Long getId() {
    return id;
  }

  public void noteCreditReserved() {
    this.state = OrderState.APPROVED;
  }

  public void noteCreditReservationFailed() {
    this.state = OrderState.REJECTED;
  }

  public OrderState getState() {
    return state;
  }

  public OrderDetails getOrderDetails() {
    return orderDetails;
  }
}
