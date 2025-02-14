package io.eventuate.examples.tram.ordersandcustomers.orders.domain;

import io.eventuate.examples.common.money.Money;

public record OrderDetails(Long customerId, Money orderTotal) {}
