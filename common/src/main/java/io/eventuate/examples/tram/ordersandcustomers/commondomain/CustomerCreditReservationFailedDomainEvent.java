package io.eventuate.examples.tram.ordersandcustomers.commondomain;

import io.eventuate.tram.events.common.DomainEvent;

public class CustomerCreditReservationFailedDomainEvent implements DomainEvent {

  private Long orderId;

  public CustomerCreditReservationFailedDomainEvent() {
  }

  public CustomerCreditReservationFailedDomainEvent(Long orderId) {
    this.orderId = orderId;
  }

  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }
}
