package io.eventuate.examples.tram.ordersandcustomers.customers.webapi;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.MoneyDTO;

public class CreateCustomerRequest {
  private String name;
  private MoneyDTO creditLimit;

  public CreateCustomerRequest() {
  }

  public CreateCustomerRequest(String name, MoneyDTO creditLimit) {

    this.name = name;
    this.creditLimit = creditLimit;
  }


  public String getName() {
    return name;
  }

  public MoneyDTO getCreditLimit() {
    return creditLimit;
  }
}
