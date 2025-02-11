package io.eventuate.examples.tram.ordersandcustomers.orders.webapi;


import io.eventuate.examples.common.money.Money;

public record CreateOrderRequest(Long customerId, Money orderTotal) {

}
