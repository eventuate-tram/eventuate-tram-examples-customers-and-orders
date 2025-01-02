package io.eventuate.examples.tram.ordersandcustomers.orderhistory.common;

import io.eventuate.examples.common.money.Money;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.events.OrderState;

public class OrderInfo {

  private Long orderId;
  private OrderState state;
  private Money orderTotal;


  public OrderInfo() {
  }

  public OrderInfo(Long orderId, Money orderTotal) {
    this.orderId = orderId;
    this.orderTotal = orderTotal;
    this.state = OrderState.PENDING;
  }

  public void approve() {
    state = OrderState.APPROVED;
  }

  public void reject() {
    state = OrderState.REJECTED;
  }

  public Money getOrderTotal() {
    return orderTotal;
  }

  public OrderState getState() {
    return state;
  }
}
