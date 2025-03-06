package io.eventuate.examples.tram.ordersandcustomers.orders.eventpublishing;

import io.eventuate.examples.tram.ordersandcustomers.orders.domain.Order;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.OrderEvent;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.OrderEventPublisher;
import io.eventuate.tram.events.publisher.AbstractDomainEventPublisherForAggregateImpl;
import io.eventuate.tram.events.publisher.DomainEventPublisher;

public class OrderEventPublisherImpl extends AbstractDomainEventPublisherForAggregateImpl<Order, Long, OrderEvent> implements OrderEventPublisher {

    public OrderEventPublisherImpl(DomainEventPublisher domainEventPublisher) {
        super(Order.class, Order::getId, domainEventPublisher, OrderEvent.class);
    }
}
