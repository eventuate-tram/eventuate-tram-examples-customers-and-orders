package io.eventuate.examples.tram.sagas.ordersandcustomers.endtoendtests.proxies.customerservice;

import java.util.List;

public record GetCustomersResponse(List<GetCustomerResponse> customers) {
}
