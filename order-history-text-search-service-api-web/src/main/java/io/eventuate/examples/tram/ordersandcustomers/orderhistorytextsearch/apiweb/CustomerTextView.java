package io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch.apiweb;


public class CustomerTextView extends TextView {

  public static final String INDEX = "customers";
  public static final String TYPE = "customer";

  private String name;
  private String creditLimit;

  public CustomerTextView() {
  }

  public CustomerTextView(String id, String name, String creditLimit) {
    super(id);
    this.name = name;
    this.creditLimit = creditLimit;
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
