package io.eventuate.examples.tram.ordersandcustomers.customers.restapi;

import java.util.List;

public record GetCustomersResponse(List<GetCustomerResponse> customers) {
}
