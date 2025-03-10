package io.eventuate.examples.tram.customersandorders.endtoendtests.proxies.customerservice;


import io.eventuate.examples.common.money.Money;

public record GetCustomerResponse(Long customerId, String name, Money creditLimit) {
}
