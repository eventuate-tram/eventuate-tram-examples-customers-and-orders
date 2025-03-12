package io.eventuate.examples.tram.ordersandcustomers.customers.restapi;

import io.eventuate.examples.common.money.Money;

public record CreateCustomerRequest(String name, Money creditLimit) {
}