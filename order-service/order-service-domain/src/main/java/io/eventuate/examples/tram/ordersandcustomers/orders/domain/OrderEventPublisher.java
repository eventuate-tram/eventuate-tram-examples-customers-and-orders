package io.eventuate.examples.tram.ordersandcustomers.orders.domain;

import io.eventuate.tram.events.publisher.DomainEventPublisherForAggregate;

public interface OrderEventPublisher extends DomainEventPublisherForAggregate<Order, Long, OrderEvent> {
}
