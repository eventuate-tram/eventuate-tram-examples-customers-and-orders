package io.eventuate.examples.tram.ordersandcustomers.orders.domain;

public record OrderRejectedEvent(OrderDetails orderDetails) implements OrderEvent {
}
