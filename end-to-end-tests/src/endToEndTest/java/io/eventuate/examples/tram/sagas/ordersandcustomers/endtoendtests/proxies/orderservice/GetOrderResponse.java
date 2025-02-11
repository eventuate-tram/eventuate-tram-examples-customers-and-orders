package io.eventuate.examples.tram.sagas.ordersandcustomers.endtoendtests.proxies.orderservice;

public record GetOrderResponse(Long orderId, OrderState orderState, RejectionReason rejectionReason) {

}
