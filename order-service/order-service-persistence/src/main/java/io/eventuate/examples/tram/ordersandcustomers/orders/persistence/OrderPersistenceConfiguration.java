package io.eventuate.examples.tram.ordersandcustomers.orders.persistence;

import io.eventuate.examples.tram.ordersandcustomers.orders.domain.Order;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.OrderRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = {OrderRepository.class})
@EntityScan(basePackageClasses = {Order.class})
public class OrderPersistenceConfiguration {

}
