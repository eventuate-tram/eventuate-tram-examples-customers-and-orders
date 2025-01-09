package io.eventuate.examples.tram.ordersandcustomers.orders.domain.events;

import io.eventuate.examples.tram.ordersandcustomers.orders.domain.common.OrderDetails;

public class OrderCancelConfirmedEvent implements OrderEvent {

  private OrderDetails orderDetails;

  public OrderCancelConfirmedEvent() {
  }

  public OrderCancelConfirmedEvent(OrderDetails orderDetails) {
    this.orderDetails = orderDetails;
  }

  public OrderDetails getOrderDetails() {
    return orderDetails;
  }
}
