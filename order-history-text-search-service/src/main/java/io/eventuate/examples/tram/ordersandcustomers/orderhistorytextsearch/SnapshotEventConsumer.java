package io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch;

import io.eventuate.examples.tram.ordersandcustomers.CustomerTextView;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.CustomerSnapshotEvent;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import org.springframework.beans.factory.annotation.Autowired;

public class SnapshotEventConsumer {

  @Autowired
  private CustomerTextViewService customerTextViewService;

  public DomainEventHandlers domainEventHandlers() {
    return DomainEventHandlersBuilder
            .forAggregateType("io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer")
            .onEvent(CustomerSnapshotEvent.class, dee -> {
              customerTextViewService.index(new CustomerTextView(dee.getEvent()));
            })
            .build();
  }
}
