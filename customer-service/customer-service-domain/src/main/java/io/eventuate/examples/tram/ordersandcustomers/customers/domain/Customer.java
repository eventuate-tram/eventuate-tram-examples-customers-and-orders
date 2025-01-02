package io.eventuate.examples.tram.ordersandcustomers.customers.domain;

import io.eventuate.examples.tram.ordersandcustomers.customers.domain.events.CustomerCreatedEvent;
import io.eventuate.examples.common.money.Money;
import io.eventuate.tram.events.publisher.ResultWithEvents;

import jakarta.persistence.*;
import java.util.Collections;
import java.util.Map;

import static java.util.Collections.singletonList;

@Entity
@Table(name="Customer")
@Access(AccessType.FIELD)
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;

  @Embedded
  private Money creditLimit;

  @ElementCollection
  private Map<Long, Money> creditReservations;

  private Long creationTime;

  @Version
  private Long version;

  Money availableCredit() {
    return creditLimit.subtract(creditReservations.values().stream().reduce(Money.ZERO, Money::add));
  }

  public Customer() {
  }

  public Customer(String name, Money creditLimit) {
    this.name = name;
    this.creditLimit = creditLimit;
    this.creditReservations = Collections.emptyMap();
    this.creationTime = System.currentTimeMillis();
  }

  public static ResultWithEvents<Customer> create(String name, Money creditLimit) {
    Customer customer = new Customer(name, creditLimit);
    return new ResultWithEvents<>(customer,
            singletonList(new CustomerCreatedEvent(customer.getName(), customer.getCreditLimit())));
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Money getCreditLimit() {
    return creditLimit;
  }

  public void reserveCredit(Long orderId, Money orderTotal) {
    if (availableCredit().isGreaterThanOrEqual(orderTotal)) {
      creditReservations.put(orderId, orderTotal);
    } else
      throw new CustomerCreditLimitExceededException();
  }

  public void unreserveCredit(long orderId) {
    creditReservations.remove(orderId);
  }
}
