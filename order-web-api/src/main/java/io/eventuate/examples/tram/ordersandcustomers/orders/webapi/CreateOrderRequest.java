package io.eventuate.examples.tram.ordersandcustomers.orders.webapi;


import io.eventuate.examples.tram.ordersandcustomers.commondomain.MoneyDTO;

public class CreateOrderRequest {
  private MoneyDTO orderTotal;
  private Long customerId;

  public CreateOrderRequest() {
  }

  public CreateOrderRequest(Long customerId, MoneyDTO orderTotal) {
    this.customerId = customerId;
    this.orderTotal = orderTotal;
  }

  public MoneyDTO getOrderTotal() {
    return orderTotal;
  }

  public Long getCustomerId() {
    return customerId;
  }
}
