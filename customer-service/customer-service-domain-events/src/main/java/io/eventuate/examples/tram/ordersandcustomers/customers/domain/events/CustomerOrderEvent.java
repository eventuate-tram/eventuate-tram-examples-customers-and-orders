package io.eventuate.examples.tram.ordersandcustomers.customers.domain.events;

public interface CustomerOrderEvent extends CustomerEvent {
  Long orderId();
}
