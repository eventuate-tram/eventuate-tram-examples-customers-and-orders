package io.eventuate.examples.tram.ordersandcustomers.customers.web;

import java.util.List;

public record GetCustomersResponse(List<GetCustomerResponse> customers) {
}
