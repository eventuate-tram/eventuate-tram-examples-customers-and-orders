package io.eventuate.examples.tram.ordersandcustomers.commondomain;

public class CustomerCreditReleasedEvent extends AbstractCustomerOrderEvent {

  public CustomerCreditReleasedEvent() {
  }

  public CustomerCreditReleasedEvent(Long orderId) {
    super(orderId);
  }
}
