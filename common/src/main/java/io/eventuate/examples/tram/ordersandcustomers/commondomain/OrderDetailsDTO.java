package io.eventuate.examples.tram.ordersandcustomers.commondomain;

public class OrderDetailsDTO {

  private Long customerId;
  private MoneyDTO orderTotal;

  public OrderDetailsDTO() {
  }

  public OrderDetailsDTO(Long customerId, MoneyDTO orderTotal) {
    this.customerId = customerId;
    this.orderTotal = orderTotal;
  }

  public Long getCustomerId() {
    return customerId;
  }

  public MoneyDTO getOrderTotal() {
    return orderTotal;
  }
}
