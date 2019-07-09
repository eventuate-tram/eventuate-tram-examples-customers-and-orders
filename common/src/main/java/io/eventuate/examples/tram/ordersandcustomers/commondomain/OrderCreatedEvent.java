package io.eventuate.examples.tram.ordersandcustomers.commondomain;

import io.eventuate.tram.events.common.DomainEvent;

public class OrderCreatedEvent implements DomainEvent {

  private OrderDetailsDTO orderDetails;

  public OrderCreatedEvent() {
  }

  public OrderCreatedEvent(OrderDetailsDTO orderDetails) {
    this.orderDetails = orderDetails;
  }

  public OrderDetailsDTO getOrderDetails() {
    return orderDetails;
  }
}
