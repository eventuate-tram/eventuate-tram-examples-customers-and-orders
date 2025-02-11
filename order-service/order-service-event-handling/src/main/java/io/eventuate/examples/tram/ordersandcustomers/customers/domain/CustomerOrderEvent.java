package io.eventuate.examples.tram.ordersandcustomers.customers.domain;

public interface CustomerOrderEvent extends CustomerEvent {
  Long orderId();
}
