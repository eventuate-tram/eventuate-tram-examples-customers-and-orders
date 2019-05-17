package io.eventuate.examples.tram.ordersandcustomers.commondomain;

import io.eventuate.tram.events.common.DomainEvent;

public abstract class AbstractCustomerOrderEvent implements DomainEvent {
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
