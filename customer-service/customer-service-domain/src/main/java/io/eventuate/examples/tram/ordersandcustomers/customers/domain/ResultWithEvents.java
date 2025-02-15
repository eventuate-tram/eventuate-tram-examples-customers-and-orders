package io.eventuate.examples.tram.ordersandcustomers.customers.domain;

import io.eventuate.tram.events.common.DomainEvent;

import java.util.List;

public record ResultWithEvents<Result, Event extends DomainEvent>(Result result, List<Event> events) {

  public ResultWithEvents(Result result, Event event) {
    this(result, List.of(event));
  }

}
