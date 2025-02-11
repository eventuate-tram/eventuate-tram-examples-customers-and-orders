package io.eventuate.examples.tram.ordersandcustomers.orders.webapi;

import java.util.List;

public record GetOrdersResponse(List<GetOrderResponse> orders) {
}
