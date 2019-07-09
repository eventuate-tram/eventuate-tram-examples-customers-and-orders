package io.eventuate.examples.tram.ordersandcustomers.commondomain;

public abstract class AbstractCustomerOrderEvent implements CustomerEvent {
  protected Long orderId;

  protected AbstractCustomerOrderEvent(Long orderId) {
    this.orderId = orderId;
  }

  protected AbstractCustomerOrderEvent() {
  }

  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }
}
