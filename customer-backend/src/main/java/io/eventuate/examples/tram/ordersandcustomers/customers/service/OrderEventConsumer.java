package io.eventuate.examples.tram.ordersandcustomers.customers.service;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.CustomerCreditReservationFailedDomainEvent;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.CustomerCreditReservedDomainEvent;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderCreatedDomainEvent;
import io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer;
import io.eventuate.examples.tram.ordersandcustomers.customers.domain.CustomerCreditLimitExceededException;
import io.eventuate.examples.tram.ordersandcustomers.customers.domain.CustomerRepository;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;


public class OrderEventConsumer {

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private DomainEventPublisher domainEventPublisher;

  public DomainEventHandlers domainEventHandlers() {
    return DomainEventHandlersBuilder
            .forAggregateType("io.eventuate.examples.tram.ordersandcustomers.orders.domain.Order")
            .onEvent(OrderCreatedDomainEvent.class, dee-> {

              OrderCreatedDomainEvent orderCreatedDomainEvent = dee.getEvent();

              Customer customer = customerRepository
                      .findOne(orderCreatedDomainEvent.getOrderDetails().getCustomerId());

              try {
                customer.reserveCredit(orderCreatedDomainEvent.getOrderId(),
                        orderCreatedDomainEvent.getOrderDetails().getOrderTotal());

                CustomerCreditReservedDomainEvent customerCreditReservedDomainEvent =
                        new CustomerCreditReservedDomainEvent(orderCreatedDomainEvent.getOrderId());

                domainEventPublisher.publish(Customer.class,
                        customer.getId(),
                        Collections.singletonList(customerCreditReservedDomainEvent));

              } catch (CustomerCreditLimitExceededException e) {

                CustomerCreditReservationFailedDomainEvent customerCreditReservationFailedDomainEvent =
                        new CustomerCreditReservationFailedDomainEvent(orderCreatedDomainEvent.getOrderId());

                domainEventPublisher.publish(Customer.class,
                        customer.getId(),
                        Collections.singletonList(customerCreditReservationFailedDomainEvent));
              }
            })
            .build();
  }
}
