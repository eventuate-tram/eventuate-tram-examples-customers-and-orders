package io.eventuate.examples.tram.ordersandcustomers.commondomain;

import io.eventuate.tram.events.common.DomainEvent;

public class OrderCreatedEvent implements DomainEvent {

  private Long orderId;

  private OrderDetails orderDetails;

  public OrderCreatedEvent() {
  }

  public OrderCreatedEvent(Long orderId, OrderDetails orderDetails) {
    this.orderId = orderId;
    this.orderDetails = orderDetails;
  }

  public OrderCreatedEvent(OrderDetails orderDetails) {
    this.orderDetails = orderDetails;
  }

  public Long getOrderId() {
    return orderId;
  }

  public OrderDetails getOrderDetails() {
    return orderDetails;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }
}
