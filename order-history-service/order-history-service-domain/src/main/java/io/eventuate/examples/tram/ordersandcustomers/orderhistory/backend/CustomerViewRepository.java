package io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerViewRepository
        extends MongoRepository<CustomerView, Long>, CustomerViewRepositoryCustom {
}
