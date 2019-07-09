package io.eventuate.examples.tram.ordersandcustomers.commondomain;

import io.eventuate.tram.events.common.DomainEvent;

public class CustomerCreatedEvent implements DomainEvent {
  private String name;
  private MoneyDTO creditLimit;

  public CustomerCreatedEvent() {
  }

  public CustomerCreatedEvent(String name, MoneyDTO creditLimit) {
    this.name = name;
    this.creditLimit = creditLimit;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public MoneyDTO getCreditLimit() {
    return creditLimit;
  }

  public void setCreditLimit(MoneyDTO creditLimit) {
    this.creditLimit = creditLimit;
  }
}
