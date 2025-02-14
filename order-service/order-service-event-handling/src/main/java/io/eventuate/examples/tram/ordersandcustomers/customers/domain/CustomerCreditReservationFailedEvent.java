package io.eventuate.examples.tram.ordersandcustomers.customers.domain;

public record CustomerCreditReservationFailedEvent(Long orderId) implements CustomerOrderEvent {
}
