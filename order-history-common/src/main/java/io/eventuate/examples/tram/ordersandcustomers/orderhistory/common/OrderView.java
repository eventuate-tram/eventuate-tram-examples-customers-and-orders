package io.eventuate.examples.tram.ordersandcustomers.orderhistory.common;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.MoneyDTO;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderState;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class OrderView {

  @Id
  private Long id;

  private OrderState state;
  private MoneyDTO orderTotal;


  public OrderView() {
  }

  public OrderView(Long id, MoneyDTO orderTotal) {
    this.id = id;
    this.orderTotal = orderTotal;
    this.state = OrderState.PENDING;
  }

  public MoneyDTO getOrderTotal() {
    return orderTotal;
  }

  public OrderState getState() {
    return state;
  }
}
