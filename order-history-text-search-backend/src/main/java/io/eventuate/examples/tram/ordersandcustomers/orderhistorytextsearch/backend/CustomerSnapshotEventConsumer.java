package io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch.backend;

import io.eventuate.examples.tram.ordersandcustomers.CustomerTextView;
import io.eventuate.examples.tram.ordersandcustomers.customers.domain.events.CustomerSnapshotEvent;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class CustomerSnapshotEventConsumer {

  @Qualifier("customerTextViewService")
  @Autowired
  private TextViewService<CustomerTextView> customerTextViewService;

  public DomainEventHandlers domainEventHandlers() {
    return DomainEventHandlersBuilder
            .forAggregateType("io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer")
            .onEvent(CustomerSnapshotEvent.class, dee -> {
              customerTextViewService.index(new CustomerTextView(dee.getEvent()));
            })
            .build();
  }
}
