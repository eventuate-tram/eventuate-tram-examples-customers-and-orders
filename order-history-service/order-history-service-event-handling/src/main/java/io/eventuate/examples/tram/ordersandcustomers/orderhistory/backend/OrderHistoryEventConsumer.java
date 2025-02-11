package io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend;

import io.eventuate.examples.tram.ordersandcustomers.customers.domain.CustomerCreatedEvent;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.OrderApprovedEvent;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.OrderCancelledEvent;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.OrderCreatedEvent;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.OrderRejectedEvent;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;


public class OrderHistoryEventConsumer {

  private final OrderHistoryViewService orderHistoryViewService;

  public OrderHistoryEventConsumer(OrderHistoryViewService orderHistoryViewService) {
    this.orderHistoryViewService = orderHistoryViewService;
  }

  public DomainEventHandlers domainEventHandlers() {
    return DomainEventHandlersBuilder
            .forAggregateType("io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer")
            .onEvent(CustomerCreatedEvent.class, this::customerCreatedEventHandler)
            .andForAggregateType("io.eventuate.examples.tram.ordersandcustomers.orders.domain.Order")
            .onEvent(OrderCreatedEvent.class, this::orderCreatedEventHandler)
            .onEvent(OrderApprovedEvent.class, this::orderApprovedEventHandler)
            .onEvent(OrderRejectedEvent.class, this::orderRejectedEventHandler)
            .onEvent(OrderCancelledEvent.class, this::handleOrderCancelledEvent)
            .build();
  }

  private void customerCreatedEventHandler(DomainEventEnvelope<CustomerCreatedEvent> domainEventEnvelope) {
    CustomerCreatedEvent customerCreatedEvent = domainEventEnvelope.getEvent();
    orderHistoryViewService.createCustomer(Long.parseLong(domainEventEnvelope.getAggregateId()),
        customerCreatedEvent.name(), customerCreatedEvent.creditLimit());
  }

  private void orderCreatedEventHandler(DomainEventEnvelope<OrderCreatedEvent> domainEventEnvelope) {
    OrderCreatedEvent orderCreatedEvent = domainEventEnvelope.getEvent();
    orderHistoryViewService.addOrder(orderCreatedEvent.orderDetails().getCustomerId(),
            Long.parseLong(domainEventEnvelope.getAggregateId()), orderCreatedEvent.orderDetails().getOrderTotal());
  }

  private void orderApprovedEventHandler(DomainEventEnvelope<OrderApprovedEvent> domainEventEnvelope) {
    OrderApprovedEvent orderApprovedEvent = domainEventEnvelope.getEvent();
    orderHistoryViewService.approveOrder(orderApprovedEvent.orderDetails().getCustomerId(),
            Long.parseLong(domainEventEnvelope.getAggregateId()));
  }

  private void orderRejectedEventHandler(DomainEventEnvelope<OrderRejectedEvent> domainEventEnvelope) {
    OrderRejectedEvent orderRejectedEvent = domainEventEnvelope.getEvent();
    orderHistoryViewService.rejectOrder(orderRejectedEvent.orderDetails().getCustomerId(),
            Long.parseLong(domainEventEnvelope.getAggregateId()));
  }

  private void handleOrderCancelledEvent(DomainEventEnvelope<OrderCancelledEvent> domainEventEnvelope) {
    OrderCancelledEvent orderRejectedEvent = domainEventEnvelope.getEvent();
    orderHistoryViewService.cancelOrder(orderRejectedEvent.orderDetails().getCustomerId(),
            Long.parseLong(domainEventEnvelope.getAggregateId()));
  }
}

