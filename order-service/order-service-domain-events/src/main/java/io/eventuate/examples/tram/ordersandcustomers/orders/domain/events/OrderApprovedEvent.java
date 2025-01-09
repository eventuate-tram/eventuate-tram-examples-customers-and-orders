package io.eventuate.examples.tram.ordersandcustomers.orders.domain.events;

import io.eventuate.examples.tram.ordersandcustomers.orders.domain.common.OrderDetails;

public class OrderApprovedEvent implements OrderEvent {

  private OrderDetails orderDetails;

  public OrderApprovedEvent() {
  }

  public OrderApprovedEvent(OrderDetails orderDetails) {
    this.orderDetails = orderDetails;
  }

  public OrderDetails getOrderDetails() {
    return orderDetails;
  }
}
