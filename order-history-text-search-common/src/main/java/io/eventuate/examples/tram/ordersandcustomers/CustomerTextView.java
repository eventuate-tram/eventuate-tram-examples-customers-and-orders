package io.eventuate.examples.tram.ordersandcustomers;


import io.eventuate.examples.tram.ordersandcustomers.customers.domain.events.CustomerSnapshotEvent;

public class CustomerTextView extends TextView {

  public static final String INDEX = "customers";
  public static final String TYPE = "customer";

  private String name;
  private String creditLimit;

  public CustomerTextView() {
  }

  public CustomerTextView(CustomerSnapshotEvent customerSnapshotEvent) {
    super(String.valueOf(customerSnapshotEvent.getId()));
    name = customerSnapshotEvent.getName();
    creditLimit = customerSnapshotEvent.getCreditLimit().getAmount().toString();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCreditLimit() {
    return creditLimit;
  }

  public void setCreditLimit(String creditLimit) {
    this.creditLimit = creditLimit;
  }
}
