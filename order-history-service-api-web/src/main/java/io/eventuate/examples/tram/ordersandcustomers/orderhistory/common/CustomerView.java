package io.eventuate.examples.tram.ordersandcustomers.orderhistory.common;

import io.eventuate.examples.tram.ordersandcustomers.common.domain.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Document
public class CustomerView {

  @Id
  private Long id;


  private Map<Long, OrderInfo> orders = new HashMap<>();
  private String name;
  private Money creditLimit;

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public Map<Long, OrderInfo> getOrders() {
    return orders;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setCreditLimit(Money creditLimit) {
    this.creditLimit = creditLimit;
  }

  public Money getCreditLimit() {
    return creditLimit;
  }
}
