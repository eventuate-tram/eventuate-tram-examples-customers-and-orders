package io.eventuate.examples.tram.sagas.ordersandcustomers.endtoendtests.proxies.orderservice;


import io.eventuate.examples.common.money.Money;

public record CreateOrderRequest(Long customerId, Money orderTotal) {

}
