package io.eventuate.examples.tram.ordersandcustomers.orders.web;

import java.util.List;

public record GetOrdersResponse(List<GetOrderResponse> orders) {
}
