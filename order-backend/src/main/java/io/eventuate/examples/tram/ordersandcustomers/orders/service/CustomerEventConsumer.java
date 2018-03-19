package io.eventuate.examples.tram.ordersandcustomers.orders.service;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.CustomerCreditReservationFailedEvent;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.CustomerCreditReservedEvent;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.OrderRepository;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.Order;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import org.springframework.beans.factory.annotation.Autowired;


public class CustomerEventConsumer {

  @Autowired
  private OrderRepository orderRepository;

  public DomainEventHandlers domainEventHandlers() {
    return DomainEventHandlersBuilder
            .forAggregateType("io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer")
            .onEvent(CustomerCreditReservedEvent.class, this::customerCreditReservedEventHandler)
            .onEvent(CustomerCreditReservationFailedEvent.class, this::customerCreditReservationFailedEventHandler)
            .build();
  }

  private void customerCreditReservedEventHandler(DomainEventEnvelope<CustomerCreditReservedEvent> domainEventEnvelope) {
    CustomerCreditReservedEvent customerCreditReservedEvent = domainEventEnvelope.getEvent();
    Order order = orderRepository.findOne(customerCreditReservedEvent.getOrderId());
    order.noteCreditReserved();
  }

  private void customerCreditReservationFailedEventHandler(DomainEventEnvelope<CustomerCreditReservationFailedEvent> domainEventEnvelope) {
    CustomerCreditReservationFailedEvent customerCreditReservationFailedEvent = domainEventEnvelope.getEvent();
    Order order = orderRepository.findOne(customerCreditReservationFailedEvent.getOrderId());
    order.noteCreditReservationFailed();
  }
}
