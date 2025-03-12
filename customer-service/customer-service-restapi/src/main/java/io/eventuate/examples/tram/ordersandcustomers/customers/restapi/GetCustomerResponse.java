package io.eventuate.examples.tram.ordersandcustomers.customers.restapi;


import io.eventuate.examples.common.money.Money;

public record GetCustomerResponse(Long customerId, String name, Money creditLimit) {
}
