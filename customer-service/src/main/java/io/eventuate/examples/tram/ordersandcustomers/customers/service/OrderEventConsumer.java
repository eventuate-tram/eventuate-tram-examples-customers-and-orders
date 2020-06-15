package io.eventuate.examples.tram.ordersandcustomers.customers.service;

import io.eventuate.examples.tram.ordersandcustomers.orders.domain.events.OrderCancelledEvent;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.events.OrderCreatedEvent;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderEventConsumer {
  private Logger logger = LoggerFactory.getLogger(getClass());

  private CustomerService customerService;

  public OrderEventConsumer(CustomerService customerService) {
    this.customerService = customerService;
  }

  public DomainEventHandlers domainEventHandlers() {
    return DomainEventHandlersBuilder
            .forAggregateType("io.eventuate.examples.tram.ordersandcustomers.orders.domain.Order")
            .onEvent(OrderCreatedEvent.class, this::handleOrderCreatedEvent)
            .onEvent(OrderCancelledEvent.class, this::handleOrderCancelledEvent)
            .build();
  }

  public void handleOrderCreatedEvent(DomainEventEnvelope<OrderCreatedEvent> domainEventEnvelope) {
    OrderCreatedEvent event = domainEventEnvelope.getEvent();
    customerService.reserveCredit(Long.parseLong(domainEventEnvelope.getAggregateId()),
            event.getOrderDetails().getCustomerId(), event.getOrderDetails().getOrderTotal());
  }

  public void handleOrderCancelledEvent(DomainEventEnvelope<OrderCancelledEvent> domainEventEnvelope) {
    customerService.releaseCredit(Long.parseLong(domainEventEnvelope.getAggregateId()), domainEventEnvelope.getEvent().getOrderDetails().getCustomerId());
  }

}
