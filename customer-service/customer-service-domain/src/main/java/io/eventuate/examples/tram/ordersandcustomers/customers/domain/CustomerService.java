package io.eventuate.examples.tram.ordersandcustomers.customers.domain;

import io.eventuate.examples.common.money.Money;
import io.eventuate.tram.events.publisher.ResultWithTypedEvents;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class CustomerService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final CustomerRepository customerRepository;

  private final CustomerEventPublisher customerEventPublisher;

  public CustomerService(CustomerRepository customerRepository, CustomerEventPublisher customerEventPublisher) {
    this.customerRepository = customerRepository;
    this.customerEventPublisher = customerEventPublisher;
  }

  @Transactional
  public Customer createCustomer(String name, Money creditLimit) {
    ResultWithTypedEvents<Customer, CustomerEvent> customerWithEvents = Customer.create(name, creditLimit);
    Customer customer = customerRepository.save(customerWithEvents.getResult());
    customerEventPublisher.publish(customer, customerWithEvents.getEvents());
    return customer;
  }

  public void reserveCredit(long orderId, long customerId, Money orderTotal) {

    Optional<Customer> possibleCustomer = customerRepository.findById(customerId);

    if (possibleCustomer.isEmpty()) {
      logger.info("Non-existent customer: {}", customerId);
      customerEventPublisher.publishById(
              customerId,
              new CustomerValidationFailedEvent(customerId, orderId));
      return;
    }

    Customer customer = possibleCustomer.get();

    try {
      customer.reserveCredit(orderId, orderTotal);

      CustomerCreditReservedEvent customerCreditReservedEvent =
              new CustomerCreditReservedEvent(customerId, orderId);

      customerEventPublisher.publish(customer, customerCreditReservedEvent);

    } catch (CustomerCreditLimitExceededException e) {

      CustomerCreditReservationFailedEvent customerCreditReservationFailedEvent =
              new CustomerCreditReservationFailedEvent(customerId, orderId);

      customerEventPublisher.publish(customer, customerCreditReservationFailedEvent);

    }
  }

  public void releaseCredit(long orderId, long customerId) {
    Customer customer = customerRepository.findById(customerId).get();
    customer.unreserveCredit(orderId);
  }

  public Iterable<Customer> findAll() {
    return customerRepository.findAll();
  }

  public Optional<Customer> findById(long customerId) {
    return customerRepository.findById(customerId);
  }
}
