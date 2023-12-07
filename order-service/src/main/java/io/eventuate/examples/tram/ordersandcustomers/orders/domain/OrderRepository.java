package io.eventuate.examples.tram.ordersandcustomers.orders.domain;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends PagingAndSortingRepository<Order, Long>, CrudRepository<Order, Long> {
}
