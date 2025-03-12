package io.eventuate.examples.tram.ordersandcustomers.orders.restapi;


import io.eventuate.examples.tram.ordersandcustomers.orders.domain.OrderState;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.RejectionReason;

public record GetOrderResponse(Long orderId, OrderState orderState, RejectionReason rejectionReason) {

}
