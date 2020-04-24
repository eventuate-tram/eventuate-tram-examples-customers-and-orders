package io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch.backend;

import io.eventuate.examples.tram.ordersandcustomers.CustomerTextView;
import io.eventuate.examples.tram.ordersandcustomers.OrderTextView;
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

  @Bean
  public TransportClient elasticSearchClient() throws Exception {
    return new PreBuiltTransportClient(Settings.builder().put("client.transport.ignore_cluster_name", true).build())
            .addTransportAddress(new TransportAddress(InetAddress.getByName(elasticSearchHost), elasticSearchPort));
  }

  @Bean("customerTextViewService")
  public TextViewService<CustomerTextView> customerTextViewService(TransportClient transportClient) {
    return new TextViewService<>(transportClient, CustomerTextView.class, CustomerTextView.TYPE, CustomerTextView.INDEX);
  }

  @Bean("orderTextViewService")
  public TextViewService<OrderTextView> orderTextViewService(TransportClient transportClient) {
    return new TextViewService<>(transportClient, OrderTextView.class, OrderTextView.TYPE, OrderTextView.INDEX);
  }
}
