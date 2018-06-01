package io.eventuate.examples.tram.ordersandcustomers.orders.service;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.CustomerCreditReservationFailedEvent;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.CustomerCreditReservedEvent;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import org.springframework.beans.factory.annotation.Autowired;


public class CustomerEventConsumer {

  @Autowired
  private OrderService orderService;

  public DomainEventHandlers domainEventHandlers() {
    return DomainEventHandlersBuilder
            .forAggregateType("io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer")
            .onEvent(CustomerCreditReservedEvent.class, this::customerCreditReservedEventHandler)
            .onEvent(CustomerCreditReservationFailedEvent.class, this::customerCreditReservationFailedEventHandler)
            .build();
  }

  private void customerCreditReservedEventHandler(DomainEventEnvelope<CustomerCreditReservedEvent> domainEventEnvelope) {
    orderService.approveOrder(domainEventEnvelope.getEvent().getOrderId());
  }

  private void customerCreditReservationFailedEventHandler(DomainEventEnvelope<CustomerCreditReservationFailedEvent> domainEventEnvelope) {
    orderService.rejectOrder(domainEventEnvelope.getEvent().getOrderId());
  }
}
