package io.eventuate.examples.tram.ordersandcustomers.customers.domain;

public interface CustomerEventPublisher extends EventPublisher<Customer, Long, CustomerEvent> {
}
