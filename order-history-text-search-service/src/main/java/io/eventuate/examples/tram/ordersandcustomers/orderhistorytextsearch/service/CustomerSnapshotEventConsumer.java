package io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch.service;

import io.eventuate.examples.tram.ordersandcustomers.customers.domain.events.CustomerSnapshotEvent;
import io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch.apiweb.CustomerTextView;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
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
            .onEvent(CustomerSnapshotEvent.class, this::handleCustomerSnapshotEvent)
            .build();
  }

  private void handleCustomerSnapshotEvent(DomainEventEnvelope<CustomerSnapshotEvent> customerSnapshotEventEventEnvelope) {
    CustomerSnapshotEvent customerSnapshotEvent = customerSnapshotEventEventEnvelope.getEvent();

    String id = String.valueOf(customerSnapshotEvent.getId());
    String name = customerSnapshotEvent.getName();
    String creditLimit = customerSnapshotEvent.getCreditLimit().getAmount().toString();

    customerTextViewService.index(new CustomerTextView(id, name, creditLimit));
  }
}
