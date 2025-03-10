package io.eventuate.examples.tram.customersandorders.endtoendtests.proxies.orderservice;

import java.util.List;

public record GetOrdersResponse(List<GetOrderResponse> orders) {
}
