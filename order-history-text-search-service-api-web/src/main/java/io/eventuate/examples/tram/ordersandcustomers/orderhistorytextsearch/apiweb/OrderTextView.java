package io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch.apiweb;

public class OrderTextView extends TextView {

  public static final String INDEX = "orders";
  public static final String TYPE = "order";

  private String customerId;
  private String orderTotal;
  private String state;

  public OrderTextView() {
  }

  public OrderTextView(String id, String customerId, String orderTotal, String state) {
    super(id);
    this.customerId = customerId;
    this.orderTotal = orderTotal;
    this.state = state;
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public String getOrderTotal() {
    return orderTotal;
  }

  public void setOrderTotal(String orderTotal) {
    this.orderTotal = orderTotal;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }
}
