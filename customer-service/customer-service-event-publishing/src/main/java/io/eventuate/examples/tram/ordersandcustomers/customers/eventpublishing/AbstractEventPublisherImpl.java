package io.eventuate.examples.tram.ordersandcustomers.customers.eventpublishing;

import io.eventuate.examples.tram.ordersandcustomers.customers.domain.EventPublisher;
import io.eventuate.tram.events.common.DomainEvent;
import io.eventuate.tram.events.publisher.DomainEventPublisher;

import java.util.List;
import java.util.function.Function;

public abstract class AbstractEventPublisherImpl<A, I, E extends DomainEvent> implements EventPublisher<A, I, E> {

  private final Class<A> aggregateClass;
  private final Function<A, I> idExtractor;
  private final DomainEventPublisher domainEventPublisher;
  private final Class<E> eventBaseClass;

  protected AbstractEventPublisherImpl(Class<A> aggregateClass,
                                       Function<A, I> idExtractor,
                                       DomainEventPublisher domainEventPublisher, Class<E> eventBaseClass) {
    this.aggregateClass = aggregateClass;
    this.idExtractor = idExtractor;
    this.domainEventPublisher = domainEventPublisher;
    this.eventBaseClass = eventBaseClass;
  }

  @Override
  public void publishById(I aggregateId, E event) {
    domainEventPublisher.publish(aggregateClass, aggregateId, List.of(event));
  }

  @Override
  public void publish(A aggregate, E event) {
    publishById(idExtractor.apply(aggregate), event);
  }

  @Override
  public void publish(A aggregate, List<E> events) {
    Long aggregateId = (Long) idExtractor.apply(aggregate);
    domainEventPublisher.publish(aggregateClass, aggregateId, (List<DomainEvent>) events);
  }

  @Override
  public Class<A> getAggregateClass() {
    return aggregateClass;
  }

  @Override
  public Class<E> getEventBaseClass() {
    return eventBaseClass;
  }
}
