package io.eventuate.examples.tram.ordersandcustomers.orders.webapi;


import io.eventuate.examples.tram.ordersandcustomers.orders.domain.common.OrderState;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.common.RejectionReason;

public record GetOrderResponse(Long orderId, OrderState orderState, RejectionReason rejectionReason) {

}
