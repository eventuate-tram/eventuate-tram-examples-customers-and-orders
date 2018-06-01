package io.eventuate.examples.tram.ordersandcustomers.commondomain;

import io.eventuate.tram.events.common.DomainEvent;

public class OrderRejectedEvent implements DomainEvent {

  private OrderDetails orderDetails;

  public OrderRejectedEvent() {
  }

  public OrderRejectedEvent(OrderDetails orderDetails) {
    this.orderDetails = orderDetails;
  }

  public OrderDetails getOrderDetails() {
    return orderDetails;
  }
}
