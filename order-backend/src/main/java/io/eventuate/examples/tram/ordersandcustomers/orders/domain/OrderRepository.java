package io.eventuate.examples.tram.ordersandcustomers.orders.domain;

import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
}
