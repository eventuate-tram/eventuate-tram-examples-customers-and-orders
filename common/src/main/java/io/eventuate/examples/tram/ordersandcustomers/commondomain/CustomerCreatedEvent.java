package io.eventuate.examples.tram.ordersandcustomers.commondomain;

import io.eventuate.tram.events.common.DomainEvent;

public class CustomerCreatedEvent implements DomainEvent {
  private Long customerId;
  private String name;
  private Money creditLimit;

  public CustomerCreatedEvent() {
  }

  public CustomerCreatedEvent(Long customerId, String name, Money creditLimit) {
    this.customerId = customerId;
    this.name = name;
    this.creditLimit = creditLimit;
  }

  public Long getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Long customerId) {
    this.customerId = customerId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Money getCreditLimit() {
    return creditLimit;
  }

  public void setCreditLimit(Money creditLimit) {
    this.creditLimit = creditLimit;
  }
}
