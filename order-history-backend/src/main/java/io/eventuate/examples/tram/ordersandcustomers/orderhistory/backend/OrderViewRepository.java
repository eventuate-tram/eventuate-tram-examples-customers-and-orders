package io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend;

import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.OrderView;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderViewRepository extends MongoRepository<OrderView, Long>, OrderViewRepositoryCustom {
}
