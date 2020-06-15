package io.eventuate.examples.tram.ordersandcustomers.orders.domain.events;

public class OrderCreatedEvent implements OrderEvent {

  private OrderDetails orderDetails;

  public OrderCreatedEvent() {
  }

  public OrderCreatedEvent(OrderDetails orderDetails) {
    this.orderDetails = orderDetails;
  }

  public OrderDetails getOrderDetails() {
    return orderDetails;
  }
}
