package io.eventuate.examples.tram.ordersandcustomers;


import io.eventuate.examples.tram.ordersandcustomers.orders.domain.events.OrderSnapshotEvent;

public class OrderTextView extends TextView {

  public static final String INDEX = "orders";
  public static final String TYPE = "order";

  private String customerId;
  private String orderTotal;
  private String state;

  public OrderTextView() {
  }

  public OrderTextView(OrderSnapshotEvent orderSnapshotEvent) {
    super(String.valueOf(orderSnapshotEvent.getId()));
    customerId = String.valueOf(orderSnapshotEvent.getCustomerId());
    orderTotal = orderSnapshotEvent.getOrderTotal().getAmount().toString();
    state = orderSnapshotEvent.getOrderState().toString();
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public String getOrderTotal() {
    return orderTotal;
  }

  public void setOrderTotal(String orderTotal) {
    this.orderTotal = orderTotal;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }
}
