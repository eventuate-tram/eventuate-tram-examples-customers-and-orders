package io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch.backend;

import io.eventuate.examples.tram.ordersandcustomers.OrderTextView;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderSnapshotEvent;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class OrderSnapshotEventConsumer {

  @Autowired
  @Qualifier("orderTextViewService")
  private TextViewService<OrderTextView> orderTextViewService;

  public DomainEventHandlers domainEventHandlers() {
    return DomainEventHandlersBuilder
            .forAggregateType("io.eventuate.examples.tram.ordersandcustomers.orders.domain.Order")
            .onEvent(OrderSnapshotEvent.class, dee -> {
              orderTextViewService.index(new OrderTextView(dee.getEvent()));
            })
            .build();
  }
}
