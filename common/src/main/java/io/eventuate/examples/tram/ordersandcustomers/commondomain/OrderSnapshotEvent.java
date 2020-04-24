package io.eventuate.examples.tram.ordersandcustomers.commondomain;

import io.eventuate.tram.events.common.DomainEvent;

public class OrderSnapshotEvent implements DomainEvent {
  private Long id;
  private Long customerId;
  private Money orderTotal;
  private OrderState orderState;

  public OrderSnapshotEvent() {
  }

  public OrderSnapshotEvent(Long id, Long customerId, Money orderTotal, OrderState orderState) {
    this.id = id;
    this.customerId = customerId;
    this.orderTotal = orderTotal;
    this.orderState = orderState;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Long customerId) {
    this.customerId = customerId;
  }

  public Money getOrderTotal() {
    return orderTotal;
  }

  public void setOrderTotal(Money orderTotal) {
    this.orderTotal = orderTotal;
  }

  public OrderState getOrderState() {
    return orderState;
  }

  public void setOrderState(OrderState orderState) {
    this.orderState = orderState;
  }
}
