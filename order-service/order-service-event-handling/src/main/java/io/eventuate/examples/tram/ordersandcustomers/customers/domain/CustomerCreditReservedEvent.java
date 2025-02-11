package io.eventuate.examples.tram.ordersandcustomers.customers.domain;

public record CustomerCreditReservedEvent(Long customerId, Long orderId) implements CustomerOrderEvent {
}
