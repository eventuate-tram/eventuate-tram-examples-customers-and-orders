package io.eventuate.examples.tram.ordersandcustomers.customers.domain;

import io.eventuate.tram.events.common.DomainEvent;

import java.util.List;

public interface EventPublisher<A, I, E extends DomainEvent> {
    void publishById(I aggregateId, E event);
    void publish(A aggregate, E event);
    void publish(A aggregate, List<E> events);

    Class<A> getAggregateClass();

    Class<E> getEventBaseClass();
}