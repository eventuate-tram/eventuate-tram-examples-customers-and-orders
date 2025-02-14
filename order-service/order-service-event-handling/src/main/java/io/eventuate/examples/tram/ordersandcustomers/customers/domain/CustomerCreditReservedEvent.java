package io.eventuate.examples.tram.ordersandcustomers.customers.domain;

public record CustomerCreditReservedEvent(Long orderId) implements CustomerOrderEvent {
}
