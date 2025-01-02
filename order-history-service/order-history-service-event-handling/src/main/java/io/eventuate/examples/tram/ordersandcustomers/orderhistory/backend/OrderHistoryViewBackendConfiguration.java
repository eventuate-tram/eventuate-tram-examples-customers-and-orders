package io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend;

import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.eventuate.tram.spring.consumer.common.TramNoopDuplicateMessageDetectorConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(TramNoopDuplicateMessageDetectorConfiguration.class)
public class OrderHistoryViewBackendConfiguration {

  @Bean
  public OrderHistoryEventConsumer customerHistoryEventConsumer(OrderHistoryViewService orderHistoryViewService) {
    return new OrderHistoryEventConsumer(orderHistoryViewService);
  }

  @Bean("customerHistoryDomainEventDispatcher")
  public DomainEventDispatcher customerHistoryDomainEventDispatcher(OrderHistoryEventConsumer orderHistoryEventConsumer, DomainEventDispatcherFactory domainEventDispatcherFactory) {
    return domainEventDispatcherFactory.make("customerHistoryServiceEvents", orderHistoryEventConsumer.domainEventHandlers());
  }
}
