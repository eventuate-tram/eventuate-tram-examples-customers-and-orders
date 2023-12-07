package io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch;

import io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch.repositories.RepositoriesConfiguration;
import io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch.service.CustomerSnapshotEventConsumer;
import io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch.service.OrderSnapshotEventConsumer;
import io.eventuate.tram.consumer.common.NoopDuplicateMessageDetector;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({NoopDuplicateMessageDetector.class, RepositoriesConfiguration.class})
public class OrderHistoryTestSearchConfiguration {

  @Value("${elasticsearch.host}")
  private String elasticSearchHost;

  @Value("${elasticsearch.port}")
  private int elasticSearchPort;

  @Bean
  public CustomerSnapshotEventConsumer customerSnapshotEventConsumer() {
    return new CustomerSnapshotEventConsumer();
  }

  @Bean
  public OrderSnapshotEventConsumer orderSnapshotEventConsumer() {
    return new OrderSnapshotEventConsumer();
  }

  @Bean("customerDomainEventDispatcher")
  public DomainEventDispatcher customerDomainEventDispatcher(CustomerSnapshotEventConsumer customerSnapshotEventConsumer, DomainEventDispatcherFactory domainEventDispatcherFactory) {
    return domainEventDispatcherFactory.make("customerTextSearchServiceEvents", customerSnapshotEventConsumer.domainEventHandlers());
  }

  @Bean("orderDomainEventDispatcher")
  public DomainEventDispatcher orderDomainEventDispatcher(OrderSnapshotEventConsumer orderSnapshotEventConsumer, DomainEventDispatcherFactory domainEventDispatcherFactory) {
    return domainEventDispatcherFactory.make("orderTextSearchServiceEvents", orderSnapshotEventConsumer.domainEventHandlers());
  }


}
