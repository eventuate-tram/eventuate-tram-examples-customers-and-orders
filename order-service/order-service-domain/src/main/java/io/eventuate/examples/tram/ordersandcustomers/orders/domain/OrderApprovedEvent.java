package io.eventuate.examples.tram.ordersandcustomers.orders.domain;

public record OrderApprovedEvent(OrderDetails orderDetails) implements OrderEvent {
}
