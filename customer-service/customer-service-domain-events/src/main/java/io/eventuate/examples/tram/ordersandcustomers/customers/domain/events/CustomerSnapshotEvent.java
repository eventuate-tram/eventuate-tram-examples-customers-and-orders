package io.eventuate.examples.tram.ordersandcustomers.customers.domain.events;

import io.eventuate.examples.common.money.Money;
import io.eventuate.tram.events.common.DomainEvent;

public class CustomerSnapshotEvent implements DomainEvent {
  private Long id;
  private String name;
  private Money creditLimit;

  public CustomerSnapshotEvent() {
  }

  public CustomerSnapshotEvent(Long id, String name, Money creditLimit) {
    this.id = id;
    this.name = name;
    this.creditLimit = creditLimit;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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
