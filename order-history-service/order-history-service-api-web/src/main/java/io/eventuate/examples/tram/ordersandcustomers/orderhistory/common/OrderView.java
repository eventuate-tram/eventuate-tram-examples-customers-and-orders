package io.eventuate.examples.tram.ordersandcustomers.orderhistory.common;

import io.eventuate.examples.common.money.Money;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.common.OrderState;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class OrderView {

  @Id
  private Long id;

  private OrderState state;
  private Money orderTotal;


  public OrderView() {
  }

  public OrderView(Long id, Money orderTotal) {
    this.id = id;
    this.orderTotal = orderTotal;
    this.state = OrderState.PENDING;
  }

  public Money getOrderTotal() {
    return orderTotal;
  }

  public OrderState getState() {
    return state;
  }
}
