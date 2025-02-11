package io.eventuate.examples.tram.ordersandcustomers.orders.domain.events;

import io.eventuate.examples.tram.ordersandcustomers.orders.domain.common.OrderDetails;

public record OrderRejectedEvent(OrderDetails orderDetails) implements OrderEvent {
}
