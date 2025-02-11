package io.eventuate.examples.tram.ordersandcustomers.customers.domain.events;

public record CustomerCreditReservationFailedEvent(Long customerId, Long orderId) implements CustomerOrderEvent {
}
