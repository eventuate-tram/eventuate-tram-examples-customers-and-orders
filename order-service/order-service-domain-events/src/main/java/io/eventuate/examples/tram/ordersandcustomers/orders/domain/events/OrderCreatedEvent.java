package io.eventuate.examples.tram.ordersandcustomers.orders.domain.events;

import io.eventuate.examples.tram.ordersandcustomers.orders.domain.common.OrderDetails;

public record OrderCreatedEvent(OrderDetails orderDetails) implements OrderEvent {
}
