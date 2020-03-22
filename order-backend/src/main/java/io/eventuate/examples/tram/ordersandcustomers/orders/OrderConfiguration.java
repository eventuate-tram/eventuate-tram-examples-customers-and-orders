package io.eventuate.examples.tram.ordersandcustomers.orders;

import io.eventuate.examples.tram.ordersandcustomers.orders.domain.OrderRepository;
import io.eventuate.examples.tram.ordersandcustomers.orders.service.CustomerEventConsumer;
import io.eventuate.examples.tram.ordersandcustomers.orders.service.OrderService;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.eventuate.tram.spring.events.subscriber.TramEventSubscriberConfiguration;
import io.eventuate.tram.spring.jdbckafka.TramJdbcKafkaConfiguration;
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
        TramEventSubscriberConfiguration.class})
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
}
