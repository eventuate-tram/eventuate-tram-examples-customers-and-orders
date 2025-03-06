package io.eventuate.examples.tram.ordersandcustomers.orders.eventhandlers;

import io.eventuate.examples.tram.ordersandcustomers.customers.domain.CustomerCreditReservationFailedEvent;
import io.eventuate.examples.tram.ordersandcustomers.customers.domain.CustomerCreditReservedEvent;
import io.eventuate.examples.tram.ordersandcustomers.customers.domain.CustomerValidationFailedEvent;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.OrderService;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.RejectionReason;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.annotations.EventuateDomainEventHandler;


public class CustomerEventConsumer {

  private final OrderService orderService;

  public CustomerEventConsumer(OrderService orderService) {
    this.orderService = orderService;
  }

  @EventuateDomainEventHandler(subscriberId = "customerServiceEvents", channel = "io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer")
  public void handleCustomerCreditReservedEvent(DomainEventEnvelope<CustomerCreditReservedEvent> domainEventEnvelope) {
    orderService.approveOrder(domainEventEnvelope.getEvent().orderId());
  }

  @EventuateDomainEventHandler(subscriberId = "customerServiceEvents", channel = "io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer")
  public void handleCustomerCreditReservationFailedEvent(DomainEventEnvelope<CustomerCreditReservationFailedEvent> domainEventEnvelope) {
    orderService.rejectOrder(domainEventEnvelope.getEvent().orderId(), RejectionReason.INSUFFICIENT_CREDIT);
  }

  @EventuateDomainEventHandler(subscriberId = "customerServiceEvents", channel = "io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer")
  public void handleCustomerValidationFailedEvent(DomainEventEnvelope<CustomerValidationFailedEvent> domainEventEnvelope) {
    orderService.rejectOrder(domainEventEnvelope.getEvent().orderId(), RejectionReason.UNKNOWN_CUSTOMER);
  }

}
