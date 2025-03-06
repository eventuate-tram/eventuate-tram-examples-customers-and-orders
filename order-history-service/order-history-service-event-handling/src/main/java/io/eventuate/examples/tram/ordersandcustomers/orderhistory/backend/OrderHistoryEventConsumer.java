package io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend;

import io.eventuate.examples.tram.ordersandcustomers.customers.domain.CustomerCreatedEvent;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.OrderApprovedEvent;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.OrderCancelledEvent;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.OrderCreatedEvent;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.OrderRejectedEvent;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.annotations.EventuateDomainEventHandler;


public class OrderHistoryEventConsumer {

  public final OrderHistoryViewService orderHistoryViewService;

  public OrderHistoryEventConsumer(OrderHistoryViewService orderHistoryViewService) {
    this.orderHistoryViewService = orderHistoryViewService;
  }

  @EventuateDomainEventHandler(subscriberId = "customerHistoryServiceEvents", channel = "io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer")
  public void customerCreatedEventHandler(DomainEventEnvelope<CustomerCreatedEvent> domainEventEnvelope) {
    CustomerCreatedEvent customerCreatedEvent = domainEventEnvelope.getEvent();
    orderHistoryViewService.createCustomer(Long.parseLong(domainEventEnvelope.getAggregateId()),
        customerCreatedEvent.name(), customerCreatedEvent.creditLimit());
  }

  @EventuateDomainEventHandler(subscriberId = "customerHistoryServiceEvents", channel = "io.eventuate.examples.tram.ordersandcustomers.orders.domain.Order")
  public void orderCreatedEventHandler(DomainEventEnvelope<OrderCreatedEvent> domainEventEnvelope) {
    OrderCreatedEvent orderCreatedEvent = domainEventEnvelope.getEvent();
    orderHistoryViewService.addOrder(orderCreatedEvent.orderDetails().customerId(),
            Long.parseLong(domainEventEnvelope.getAggregateId()), orderCreatedEvent.orderDetails().orderTotal());
  }

  @EventuateDomainEventHandler(subscriberId = "customerHistoryServiceEvents", channel = "io.eventuate.examples.tram.ordersandcustomers.orders.domain.Order")
  public void orderApprovedEventHandler(DomainEventEnvelope<OrderApprovedEvent> domainEventEnvelope) {
    OrderApprovedEvent orderApprovedEvent = domainEventEnvelope.getEvent();
    orderHistoryViewService.approveOrder(orderApprovedEvent.orderDetails().customerId(),
            Long.parseLong(domainEventEnvelope.getAggregateId()));
  }

  @EventuateDomainEventHandler(subscriberId = "customerHistoryServiceEvents", channel = "io.eventuate.examples.tram.ordersandcustomers.orders.domain.Order")
  public void orderRejectedEventHandler(DomainEventEnvelope<OrderRejectedEvent> domainEventEnvelope) {
    OrderRejectedEvent orderRejectedEvent = domainEventEnvelope.getEvent();
    orderHistoryViewService.rejectOrder(orderRejectedEvent.orderDetails().customerId(),
            Long.parseLong(domainEventEnvelope.getAggregateId()));
  }

  @EventuateDomainEventHandler(subscriberId = "customerHistoryServiceEvents", channel = "io.eventuate.examples.tram.ordersandcustomers.orders.domain.Order")
  public void handleOrderCancelledEvent(DomainEventEnvelope<OrderCancelledEvent> domainEventEnvelope) {
    OrderCancelledEvent orderRejectedEvent = domainEventEnvelope.getEvent();
    orderHistoryViewService.cancelOrder(orderRejectedEvent.orderDetails().customerId(),
            Long.parseLong(domainEventEnvelope.getAggregateId()));
  }
}
