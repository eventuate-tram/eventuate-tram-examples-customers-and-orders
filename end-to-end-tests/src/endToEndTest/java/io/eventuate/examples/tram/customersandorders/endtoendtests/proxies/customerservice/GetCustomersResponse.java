package io.eventuate.examples.tram.customersandorders.endtoendtests.proxies.customerservice;

import java.util.List;

public record GetCustomersResponse(List<GetCustomerResponse> customers) {
}
