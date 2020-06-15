package io.eventuate.examples.tram.ordersandcustomers.customers.webapi;

import io.eventuate.examples.tram.ordersandcustomers.common.domain.Money;

public class CreateCustomerRequest {
  private String name;
  private Money creditLimit;

  public CreateCustomerRequest() {
  }

  public CreateCustomerRequest(String name, Money creditLimit) {

    this.name = name;
    this.creditLimit = creditLimit;
  }


  public String getName() {
    return name;
  }

  public Money getCreditLimit() {
    return creditLimit;
  }
}
