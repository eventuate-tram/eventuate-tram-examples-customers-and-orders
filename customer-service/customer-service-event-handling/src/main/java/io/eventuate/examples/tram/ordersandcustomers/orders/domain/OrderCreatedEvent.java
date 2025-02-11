package io.eventuate.examples.tram.ordersandcustomers.orders.domain;

public record OrderCreatedEvent(OrderDetails orderDetails) implements OrderEvent {
}
