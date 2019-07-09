package io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.MoneyDTO;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderState;

public interface CustomerViewRepositoryCustom {

  void addCustomer(Long customerId, String customerName, MoneyDTO creditLimit);

  void addOrder(Long customerId, Long orderId, MoneyDTO orderTotal);

  void updateOrderState(Long customerId, Long orderId, OrderState state);
}
