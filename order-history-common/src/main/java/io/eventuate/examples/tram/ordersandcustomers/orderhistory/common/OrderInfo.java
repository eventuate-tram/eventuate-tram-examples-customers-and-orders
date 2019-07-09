package io.eventuate.examples.tram.ordersandcustomers.orderhistory.common;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.MoneyDTO;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderState;

public class OrderInfo {

  private OrderState state;
  private Long orderId;
  private MoneyDTO orderTotal;


  public OrderInfo() {
  }

  public OrderInfo(Long orderId, MoneyDTO orderTotal) {
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

  public MoneyDTO getOrderTotal() {
    return orderTotal;
  }

  public OrderState getState() {
    return state;
  }
}
