package io.eventuate.examples.tram.ordersandcustomers.customers;

import io.eventuate.examples.tram.ordersandcustomers.customers.service.CustomerService;
import io.eventuate.examples.tram.ordersandcustomers.customers.service.OrderEventConsumer;
import io.eventuate.tram.consumer.common.TramNoopDuplicateMessageDetectorConfiguration;
import io.eventuate.tram.events.publisher.TramEventsPublisherConfiguration;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.eventuate.tram.events.subscriber.TramEventSubscriberConfiguration;
import io.eventuate.tram.jdbckafka.TramJdbcKafkaConfiguration;
import io.eventuate.tram.optimisticlocking.OptimisticLockingDecoratorConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@Import({TramJdbcKafkaConfiguration.class,
        TramEventsPublisherConfiguration.class,
        TramEventSubscriberConfiguration.class,
        OptimisticLockingDecoratorConfiguration.class})
@EnableJpaRepositories
@EnableAutoConfiguration
public class CustomerConfiguration {

  @Bean
  public OrderEventConsumer orderEventConsumer() {
    return new OrderEventConsumer();
  }

  @Bean
  public DomainEventDispatcher domainEventDispatcher(OrderEventConsumer orderEventConsumer, DomainEventDispatcherFactory domainEventDispatcherFactory) {
    return domainEventDispatcherFactory.make("orderServiceEvents", orderEventConsumer.domainEventHandlers());
  }

  @Bean
  public CustomerService customerService() {
    return new CustomerService();
  }
}
