package io.eventuate.examples.tram.ordersandcustomers.customers.web;


import io.eventuate.examples.common.money.Money;

public record GetCustomerResponse(Long customerId, String name, Money creditLimit) {
}
