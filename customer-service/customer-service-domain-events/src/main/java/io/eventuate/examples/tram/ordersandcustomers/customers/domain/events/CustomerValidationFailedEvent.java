package io.eventuate.examples.tram.ordersandcustomers.customers.domain.events;

public record CustomerValidationFailedEvent(Long customerId, Long orderId) implements CustomerOrderEvent {
}
