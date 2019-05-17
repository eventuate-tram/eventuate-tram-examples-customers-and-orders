package io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend;

import io.eventuate.tram.consumer.common.NoopDuplicateMessageDetector;
import io.eventuate.tram.consumer.kafka.TramConsumerKafkaConfiguration;
import io.eventuate.tram.events.publisher.TramEventsPublisherConfiguration;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.messaging.consumer.MessageConsumer;
import io.eventuate.tram.messaging.producer.jdbc.TramMessageProducerJdbcConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({OrderHistoryViewMongoConfiguration.class,
        TramConsumerKafkaConfiguration.class,
        TramEventsPublisherConfiguration.class,
        TramMessageProducerJdbcConfiguration.class,
        NoopDuplicateMessageDetector.class})
public class OrderHistoryViewBackendConfiguration {

  @Bean
  public OrderHistoryEventConsumer orderEventConsumer() {
    return new OrderHistoryEventConsumer();
  }

  @Bean("orderHistoryDomainEventDispatcher")
  public DomainEventDispatcher orderHistoryDomainEventDispatcher(OrderHistoryEventConsumer orderHistoryEventConsumer,
                                                                 MessageConsumer messageConsumer) {

    return new DomainEventDispatcher("orderHistoryServiceEvents",
            orderHistoryEventConsumer.domainEventHandlers(), messageConsumer);
  }

  @Bean
  public CustomerHistoryEventConsumer customerHistoryEventConsumer() {
    return new CustomerHistoryEventConsumer();
  }

  @Bean("customerHistoryDomainEventDispatcher")
  public DomainEventDispatcher customerHistoryDomainEventDispatcher(CustomerHistoryEventConsumer customerHistoryEventConsumer,
                                                                    MessageConsumer messageConsumer) {

    return new DomainEventDispatcher("customerHistoryServiceEvents",
            customerHistoryEventConsumer.domainEventHandlers(), messageConsumer);
  }
}
