package io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch.apiweb;


import org.springframework.data.annotation.Id;

public abstract class TextView {

  @Id
  private String id;

  public TextView() {
  }

  public TextView(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
