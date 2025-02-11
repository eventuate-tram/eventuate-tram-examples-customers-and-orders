package io.eventuate.examples.tram.ordersandcustomers.customers.webapi;

import java.util.List;

public record GetCustomersResponse(List<GetCustomerResponse> customers) {
}
