package io.eventuate.examples.tram.sagas.ordersandcustomers.endtoendtests.proxies.orderservice;

import java.util.List;

public record GetOrdersResponse(List<GetOrderResponse> orders) {
}
