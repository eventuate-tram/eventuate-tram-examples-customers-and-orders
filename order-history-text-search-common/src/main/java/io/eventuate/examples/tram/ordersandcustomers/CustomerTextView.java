package io.eventuate.examples.tram.ordersandcustomers;


import io.eventuate.examples.tram.ordersandcustomers.commondomain.CustomerSnapshotEvent;

public class CustomerTextView {

  public static final String INDEX = "customers";
  public static final String TYPE = "customer";

  private String id;

  private String name;

  public CustomerTextView() {
  }

  public CustomerTextView(CustomerSnapshotEvent customerSnapshotEvent) {
    id = String.valueOf(customerSnapshotEvent.getId());
    name = customerSnapshotEvent.getName();
  }

  public CustomerTextView(String id, String name) {
    this.id = id;
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
