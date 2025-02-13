package io.eventuate.examples.tram.ordersandcustomers.orders.domain;

import io.eventuate.examples.common.money.Money;
import jakarta.persistence.Embeddable;

@Embeddable
public record OrderDetails(Long customerId, Money orderTotal) {}
