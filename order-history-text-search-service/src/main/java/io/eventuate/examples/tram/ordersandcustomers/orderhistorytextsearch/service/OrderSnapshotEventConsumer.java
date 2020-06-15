package io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch.service;

import io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch.apiweb.OrderTextView;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.events.OrderSnapshotEvent;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
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
            .onEvent(OrderSnapshotEvent.class, this::handleEvent)
            .build();
  }

  private void handleEvent(DomainEventEnvelope<OrderSnapshotEvent> orderSnapshotEventEventEnvelope) {

    OrderSnapshotEvent orderSnapshotEvent = orderSnapshotEventEventEnvelope.getEvent();

    String id = String.valueOf(orderSnapshotEvent.getId());
    String customerId = String.valueOf(orderSnapshotEvent.getCustomerId());
    String orderTotal = orderSnapshotEvent.getOrderTotal().getAmount().toString();
    String state = orderSnapshotEvent.getOrderState().toString();

    orderTextViewService.index(new OrderTextView(id, customerId, orderTotal, state));
  }
}
