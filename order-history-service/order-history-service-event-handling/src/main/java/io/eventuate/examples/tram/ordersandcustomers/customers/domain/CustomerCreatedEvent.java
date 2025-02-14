package io.eventuate.examples.tram.ordersandcustomers.customers.domain;

import io.eventuate.examples.common.money.Money;

public record CustomerCreatedEvent(String name, Money creditLimit) implements CustomerEvent {
}
