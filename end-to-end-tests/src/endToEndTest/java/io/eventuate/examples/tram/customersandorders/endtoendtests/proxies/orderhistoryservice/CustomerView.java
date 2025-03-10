package io.eventuate.examples.tram.customersandorders.endtoendtests.proxies.orderhistoryservice;

import io.eventuate.examples.common.money.Money;

import java.util.HashMap;
import java.util.Map;

public class CustomerView {

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
