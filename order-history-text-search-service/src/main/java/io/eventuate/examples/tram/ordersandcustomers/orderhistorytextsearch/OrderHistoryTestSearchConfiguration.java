package io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch;

import io.eventuate.tram.consumer.common.NoopDuplicateMessageDetector;
import io.eventuate.tram.spring.consumer.kafka.EventuateTramKafkaMessageConsumerConfiguration;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.eventuate.tram.spring.events.subscriber.TramEventSubscriberConfiguration;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.net.InetAddress;

@Configuration
@Import({EventuateTramKafkaMessageConsumerConfiguration.class, TramEventSubscriberConfiguration.class, NoopDuplicateMessageDetector.class})
public class OrderHistoryTestSearchConfiguration {

  @Value("${elasticsearch.host}")
  private String elasticSearchHost;

  @Value("${elasticsearch.port}")
  private int elasticSearchPort;

  @Bean
  public SnapshotEventConsumer snapshotEventConsumer() {
    return new SnapshotEventConsumer();
  }

  @Bean
  public DomainEventDispatcher domainEventDispatcher(SnapshotEventConsumer snapshotEventConsumer, DomainEventDispatcherFactory domainEventDispatcherFactory) {
    return domainEventDispatcherFactory.make("textSearchServiceEvents", snapshotEventConsumer.domainEventHandlers());
  }

  @Bean
  public TransportClient elasticSearchClient() throws Exception {
    return new PreBuiltTransportClient(Settings.builder().put("client.transport.ignore_cluster_name", true).build())
            .addTransportAddress(new TransportAddress(InetAddress.getByName(elasticSearchHost), elasticSearchPort));
  }
}
