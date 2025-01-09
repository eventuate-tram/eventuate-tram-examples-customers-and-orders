package io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend;

import io.eventuate.examples.common.money.Money;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.common.OrderState;

public interface CustomerViewRepositoryCustom {

  void addCustomer(Long customerId, String customerName, Money creditLimit);

  void addOrder(Long customerId, Long orderId, Money orderTotal);

  void updateOrderState(Long customerId, Long orderId, OrderState state);
}
