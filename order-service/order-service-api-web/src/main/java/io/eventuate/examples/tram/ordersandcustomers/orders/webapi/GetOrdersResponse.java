package io.eventuate.examples.tram.ordersandcustomers.orders.webapi;

import java.util.List;

public class GetOrdersResponse {

  private List<GetOrderResponse> orders;

  public GetOrdersResponse() {
  }

  public GetOrdersResponse(List<GetOrderResponse> orders) {
    this.orders = orders;
  }

  public List<GetOrderResponse> getOrders() {
    return orders;
  }

  public void setOrders(List<GetOrderResponse> orders) {
    this.orders = orders;
  }
}
