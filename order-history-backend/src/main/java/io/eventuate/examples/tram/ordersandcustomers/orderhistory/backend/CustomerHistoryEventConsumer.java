package io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.CustomerCreatedEvent;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.CustomerCreditReservationFailedEvent;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.CustomerCreditReservedEvent;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import org.springframework.beans.factory.annotation.Autowired;


public class CustomerHistoryEventConsumer {

  @Autowired
  private OrderHistoryViewService orderHistoryViewService;


  public DomainEventHandlers domainEventHandlers() {
    return DomainEventHandlersBuilder
            .forAggregateType("io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer")
            .onEvent(CustomerCreatedEvent.class, this::customerCreatedEventHandler)
            .onEvent(CustomerCreditReservedEvent.class, this::customerCreditReservedEventHandler)
            .onEvent(CustomerCreditReservationFailedEvent.class, this::customerCreditReservationFailedEventHandler)
            .build();
  }

  private void customerCreatedEventHandler(DomainEventEnvelope<CustomerCreatedEvent> domainEventEnvelope) {
    CustomerCreatedEvent customerCreatedEvent = domainEventEnvelope.getEvent();
    orderHistoryViewService.createCustomer(customerCreatedEvent.getCustomerId(),
            customerCreatedEvent.getName(), customerCreatedEvent.getCreditLimit());
  }

  private void customerCreditReservedEventHandler(DomainEventEnvelope<CustomerCreditReservedEvent> domainEventEnvelope) {
    CustomerCreditReservedEvent customerCreditReservedEvent = domainEventEnvelope.getEvent();
    orderHistoryViewService.approveOrder(customerCreditReservedEvent.getOrderDetails().getCustomerId(),
            customerCreditReservedEvent.getOrderId());
  }

  private void customerCreditReservationFailedEventHandler(DomainEventEnvelope<CustomerCreditReservationFailedEvent> domainEventEnvelope) {
    CustomerCreditReservationFailedEvent customerCreditReservationFailedEvent = domainEventEnvelope.getEvent();
    orderHistoryViewService.rejectOrder(customerCreditReservationFailedEvent.getOrderDetails().getCustomerId(),
            customerCreditReservationFailedEvent.getOrderId());
  }
}
