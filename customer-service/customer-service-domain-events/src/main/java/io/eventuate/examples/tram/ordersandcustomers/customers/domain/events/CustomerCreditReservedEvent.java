package io.eventuate.examples.tram.ordersandcustomers.customers.domain.events;

public record CustomerCreditReservedEvent(Long customerId, Long orderId) implements CustomerOrderEvent {
}
