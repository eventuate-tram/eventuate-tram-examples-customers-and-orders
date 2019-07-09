package io.eventuate.examples.tram.ordersandcustomers.customers.service;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.MoneyDTO;
import io.eventuate.examples.tram.ordersandcustomers.customers.EventuateMicronautJpaTransactionManagement;
import io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.publisher.ResultWithEvents;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CustomerService {

  @Inject
  private EventuateMicronautJpaTransactionManagement eventuateMicronautJpaTransactionManagement;

  @Inject
  private DomainEventPublisher domainEventPublisher;

  public Customer createCustomer(String name, MoneyDTO creditLimit) {
    return eventuateMicronautJpaTransactionManagement.doWithJpaTransaction(entityManager -> {
      ResultWithEvents<Customer> customerWithEvents = Customer.create(name, creditLimit);
      Customer customer = customerWithEvents.result;
      entityManager.persist(customer);
      domainEventPublisher.publish(Customer.class, customer.getId(), customerWithEvents.events);
      return customer;
    });
  }
}
