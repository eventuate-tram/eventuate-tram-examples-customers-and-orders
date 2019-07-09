package io.eventuate.examples.tram.ordersandcustomers.commondomain;

import io.eventuate.tram.events.common.DomainEvent;

public class OrderApprovedEvent implements DomainEvent {

  private OrderDetailsDTO orderDetails;

  public OrderApprovedEvent() {
  }

  public OrderApprovedEvent(OrderDetailsDTO orderDetails) {
    this.orderDetails = orderDetails;
  }

  public OrderDetailsDTO getOrderDetails() {
    return orderDetails;
  }
}
