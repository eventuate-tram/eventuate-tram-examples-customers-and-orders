package io.eventuate.examples.tram.ordersandcustomers.orders.service;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.CustomerCreditReservationFailedDomainEvent;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.CustomerCreditReservedDomainEvent;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.OrderRepository;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.Order;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import org.springframework.beans.factory.annotation.Autowired;


public class CustomerEventConsumer {

  @Autowired
  private OrderRepository orderRepository;

  public DomainEventHandlers domainEventHandlers() {
    return DomainEventHandlersBuilder
            .forAggregateType("io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer")
            .onEvent(CustomerCreditReservedDomainEvent.class, dee-> {
              CustomerCreditReservedDomainEvent customerCreditReservedDomainEvent = dee.getEvent();
              Order order = orderRepository.findOne(customerCreditReservedDomainEvent.getOrderId());
              order.noteCreditReserved();
            })
            .onEvent(CustomerCreditReservationFailedDomainEvent.class, dee-> {
              CustomerCreditReservationFailedDomainEvent customerCreditReservationFailedDomainEvent = dee.getEvent();
              Order order = orderRepository.findOne(customerCreditReservationFailedDomainEvent.getOrderId());
              order.noteCreditReservationFailed();
            })
            .build();
  }
}
