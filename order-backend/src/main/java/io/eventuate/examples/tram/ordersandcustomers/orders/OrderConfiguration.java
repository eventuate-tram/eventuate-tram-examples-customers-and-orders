package io.eventuate.examples.tram.ordersandcustomers.orders;

import io.eventuate.examples.tram.ordersandcustomers.orders.domain.events.OrderSnapshotEvent;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.Order;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.OrderRepository;
import io.eventuate.examples.tram.ordersandcustomers.orders.service.CustomerEventConsumer;
import io.eventuate.examples.tram.ordersandcustomers.orders.service.OrderService;
import io.eventuate.tram.events.common.DomainEvent;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.eventuate.tram.spring.events.subscriber.TramEventSubscriberConfiguration;
import io.eventuate.tram.spring.jdbckafka.TramJdbcKafkaConfiguration;
import io.eventuate.tram.spring.optimisticlocking.OptimisticLockingDecoratorConfiguration;
import io.eventuate.tram.viewsupport.rebuild.*;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories
@EnableAutoConfiguration
@Import({TramJdbcKafkaConfiguration.class,
        TramEventsPublisherConfiguration.class,
        TramEventSubscriberConfiguration.class,
        OptimisticLockingDecoratorConfiguration.class,
        SnapshotConfiguration.class})
public class OrderConfiguration {

  @Bean
  public OrderService orderService(DomainEventPublisher domainEventPublisher, OrderRepository orderRepository) {
    return new OrderService(domainEventPublisher, orderRepository);
  }


  @Bean
  public CustomerEventConsumer orderEventConsumer() {
    return new CustomerEventConsumer();
  }

  @Bean
  public DomainEventDispatcher domainEventDispatcher(CustomerEventConsumer customerEventConsumer, DomainEventDispatcherFactory domainEventDispatcherFactory) {
    return domainEventDispatcherFactory.make("customerServiceEvents", customerEventConsumer.domainEventHandlers());
  }

  @Bean
  public DomainSnapshotExportService<Order> domainSnapshotExportService(OrderRepository orderRepository,
                                                                        DomainSnapshotExportServiceFactory<Order> domainSnapshotExportServiceFactory) {
    return domainSnapshotExportServiceFactory.make(
            Order.class,
            orderRepository,
            order -> {
              DomainEvent domainEvent = new OrderSnapshotEvent(order.getId(),
                      order.getOrderDetails().getCustomerId(),
                      order.getOrderDetails().getOrderTotal(),
                      order.getState());

              return new DomainEventWithEntityId(order.getId(), domainEvent);
            },
            new DBLockService.TableSpec("orders", "order0_"),
            "MySqlReader");
  }
}
