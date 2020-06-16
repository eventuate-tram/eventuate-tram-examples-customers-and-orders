package io.eventuate.examples.tram.ordersandcustomers.orders.domain.events;

public class OrderRejectedEvent implements OrderEvent {

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
