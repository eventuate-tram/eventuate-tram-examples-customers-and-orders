package io.eventuate.examples.tram.ordersandcustomers.customers.service;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.*;
import io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer;
import io.eventuate.examples.tram.ordersandcustomers.customers.domain.CustomerCreditLimitExceededException;
import io.eventuate.examples.tram.ordersandcustomers.customers.domain.CustomerRepository;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Optional;

public class OrderEventConsumer {
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private DomainEventPublisher domainEventPublisher;

  public DomainEventHandlers domainEventHandlers() {
    return DomainEventHandlersBuilder
            .forAggregateType("io.eventuate.examples.tram.ordersandcustomers.orders.domain.Order")
            .onEvent(OrderCreatedEvent.class, this::handleOrderCreatedEventHandler)
            .onEvent(OrderCancelledEvent.class, this::handleOrderCancelledEvent)
            .build();
  }

  public void handleOrderCreatedEventHandler(DomainEventEnvelope<OrderCreatedEvent> domainEventEnvelope) {

    Long orderId = Long.parseLong(domainEventEnvelope.getAggregateId());

    OrderCreatedEvent orderCreatedEvent = domainEventEnvelope.getEvent();

    Long customerId = orderCreatedEvent.getOrderDetails().getCustomerId();

    Optional<Customer> possibleCustomer = customerRepository.findById(customerId);

    if (!possibleCustomer.isPresent()) {
      logger.info("Non-existent customer: {}", customerId);
      domainEventPublisher.publish(Customer.class,
              customerId,
              Collections.singletonList(new CustomerValidationFailedEvent(orderId)));
      return;
    }

    Customer customer = possibleCustomer.get();


    try {
      customer.reserveCredit(orderId, orderCreatedEvent.getOrderDetails().getOrderTotal());

      CustomerCreditReservedEvent customerCreditReservedEvent =
              new CustomerCreditReservedEvent(orderId);

      domainEventPublisher.publish(Customer.class,
              customer.getId(),
              Collections.singletonList(customerCreditReservedEvent));

    } catch (CustomerCreditLimitExceededException e) {

      CustomerCreditReservationFailedEvent customerCreditReservationFailedEvent =
              new CustomerCreditReservationFailedEvent(orderId);

      domainEventPublisher.publish(Customer.class,
              customer.getId(),
              Collections.singletonList(customerCreditReservationFailedEvent));
    }
  }

  public void handleOrderCancelledEvent(DomainEventEnvelope<OrderCancelledEvent> domainEventEnvelope) {

    long orderId = Long.parseLong(domainEventEnvelope.getAggregateId());

    OrderCancelledEvent orderCreatedEvent = domainEventEnvelope.getEvent();

    long customerId = orderCreatedEvent.getOrderDetails().getCustomerId();

    Customer customer = customerRepository.findById(customerId).get();

    customer.unreserveCredit(orderId);
  }
}
