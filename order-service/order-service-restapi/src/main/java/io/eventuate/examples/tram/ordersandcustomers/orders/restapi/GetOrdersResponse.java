package io.eventuate.examples.tram.ordersandcustomers.orders.restapi;

import java.util.List;

public record GetOrdersResponse(List<GetOrderResponse> orders) {
}
