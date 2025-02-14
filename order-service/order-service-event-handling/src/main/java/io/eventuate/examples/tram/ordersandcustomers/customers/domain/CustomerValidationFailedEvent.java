package io.eventuate.examples.tram.ordersandcustomers.customers.domain;

public record CustomerValidationFailedEvent(Long orderId) implements CustomerOrderEvent {
}
