package io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend;

import io.eventuate.messaging.kafka.consumer.MessageConsumerKafkaConfiguration;
import io.eventuate.tram.consumer.common.TramConsumerCommonConfiguration;
import io.eventuate.tram.consumer.common.TramNoopDuplicateMessageDetectorConfiguration;
import io.eventuate.tram.consumer.kafka.EventuateTramKafkaMessageConsumerConfiguration;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.eventuate.tram.events.subscriber.TramEventSubscriberConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({OrderHistoryViewMongoConfiguration.class,
        EventuateTramKafkaMessageConsumerConfiguration.class,
        MessageConsumerKafkaConfiguration.class, // FIXME
        TramConsumerCommonConfiguration.class, // FIXME
        TramNoopDuplicateMessageDetectorConfiguration.class,
        TramEventSubscriberConfiguration.class})
public class OrderHistoryViewBackendConfiguration {

  @Bean
  public OrderHistoryEventConsumer orderEventConsumer() {
    return new OrderHistoryEventConsumer();
  }

  @Bean("orderHistoryDomainEventDispatcher")
  public DomainEventDispatcher orderHistoryDomainEventDispatcher(OrderHistoryEventConsumer orderHistoryEventConsumer, DomainEventDispatcherFactory domainEventDispatcherFactory) {
    return domainEventDispatcherFactory.make("orderHistoryServiceEvents", orderHistoryEventConsumer.domainEventHandlers());
  }

  @Bean
  public CustomerHistoryEventConsumer customerHistoryEventConsumer() {
    return new CustomerHistoryEventConsumer();
  }

  @Bean("customerHistoryDomainEventDispatcher")
  public DomainEventDispatcher customerHistoryDomainEventDispatcher(CustomerHistoryEventConsumer customerHistoryEventConsumer, DomainEventDispatcherFactory domainEventDispatcherFactory) {
    return domainEventDispatcherFactory.make("customerHistoryServiceEvents", customerHistoryEventConsumer.domainEventHandlers());
  }
}
