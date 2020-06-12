package io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend;

import io.eventuate.examples.tram.ordersandcustomers.common.domain.Money;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.events.OrderState;

public interface CustomerViewRepositoryCustom {

  void addCustomer(Long customerId, String customerName, Money creditLimit);

  void addOrder(Long customerId, Long orderId, Money orderTotal);

  void updateOrderState(Long customerId, Long orderId, OrderState state);
}
