package io.eventuate.examples.tram.customersandorders.endtoendtests.proxies.orderservice;

public record GetOrderResponse(Long orderId, OrderState orderState, RejectionReason rejectionReason) {

}
