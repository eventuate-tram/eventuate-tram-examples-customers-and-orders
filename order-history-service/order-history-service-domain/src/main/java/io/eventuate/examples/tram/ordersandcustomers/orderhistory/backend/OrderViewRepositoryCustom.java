package io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend;

import io.eventuate.examples.common.money.Money;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.events.OrderState;

public interface OrderViewRepositoryCustom {
  void addOrder(Long orderId, Money orderTotal);
  void updateOrderState(Long orderId, OrderState state);
}
