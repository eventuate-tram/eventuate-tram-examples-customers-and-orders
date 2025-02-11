package io.eventuate.examples.tram.ordersandcustomers.customers.domain.events;

import io.eventuate.tram.events.common.DomainEvent;

public interface CustomerEvent extends DomainEvent {
  Long customerId();
}
