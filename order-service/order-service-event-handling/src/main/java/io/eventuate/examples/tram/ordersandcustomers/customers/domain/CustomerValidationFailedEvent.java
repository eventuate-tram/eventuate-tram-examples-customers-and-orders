package io.eventuate.examples.tram.ordersandcustomers.customers.domain;

public record CustomerValidationFailedEvent(Long customerId, Long orderId) implements CustomerOrderEvent {
}
