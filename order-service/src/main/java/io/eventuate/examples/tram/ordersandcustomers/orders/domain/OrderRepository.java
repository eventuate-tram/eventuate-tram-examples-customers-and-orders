package io.eventuate.examples.tram.ordersandcustomers.orders.domain;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {
}
