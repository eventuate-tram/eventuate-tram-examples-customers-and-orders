package io.eventuate.examples.tram.ordersandcustomers.customers.domain;

public record CustomerCreditReservationFailedEvent(Long customerId, Long orderId) implements CustomerOrderEvent {
}
