package io.eventuate.examples.tram.ordersandcustomers.orders.domain;

public record OrderCancelledEvent(OrderDetails orderDetails) implements OrderEvent {
}
