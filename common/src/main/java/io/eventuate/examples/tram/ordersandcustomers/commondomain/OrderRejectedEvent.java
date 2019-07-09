package io.eventuate.examples.tram.ordersandcustomers.commondomain;

import io.eventuate.tram.events.common.DomainEvent;

public class OrderRejectedEvent implements DomainEvent {

  private OrderDetailsDTO orderDetails;

  public OrderRejectedEvent() {
  }

  public OrderRejectedEvent(OrderDetailsDTO orderDetails) {
    this.orderDetails = orderDetails;
  }

  public OrderDetailsDTO getOrderDetails() {
    return orderDetails;
  }
}
