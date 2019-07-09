package io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.MoneyDTO;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderState;

public interface OrderViewRepositoryCustom {
  void addOrder(Long orderId, MoneyDTO orderTotal);
  void updateOrderState(Long orderId, OrderState state);
}
