package io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch.backend;

import io.eventuate.examples.tram.ordersandcustomers.CustomerTextView;
import io.eventuate.examples.tram.ordersandcustomers.OrderTextView;
import io.eventuate.tram.consumer.elasticsearch.ElasticsearchIndexDuplicateMessageDetector;
import io.eventuate.tram.consumer.kafka.elasticsearch.ElasticsearchKafkaConsumerFactorySpringConfiguration;
import io.eventuate.tram.consumer.kafka.elasticsearch.EventuateKafkaConsumerElasticsearchSpringConfigurationPropertiesConfiguration;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.eventuate.tram.spring.consumer.elasticsearch.ElasticsearchConsumerSpringConfigurationPropertiesConfiguration;
import io.eventuate.tram.spring.consumer.kafka.EventuateTramKafkaMessageConsumerConfiguration;
import io.eventuate.tram.spring.events.subscriber.TramEventSubscriberConfiguration;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        ElasticsearchConsumerSpringConfigurationPropertiesConfiguration.class,
        EventuateKafkaConsumerElasticsearchSpringConfigurationPropertiesConfiguration.class,
        ElasticsearchKafkaConsumerFactorySpringConfiguration.class,
        EventuateTramKafkaMessageConsumerConfiguration.class,
        TramEventSubscriberConfiguration.class,
        ElasticsearchIndexDuplicateMessageDetector.class,
        SupportingIndicesConfiguration.class
})
public class OrderHistoryTestSearchConfiguration {

  @Value("${elasticsearch.host}")
  private String elasticSearchHost;

  @Value("${elasticsearch.port}")
  private int elasticSearchPort;

  @Value("${elasticsearch.scheme}")
  private String elasticSearchScheme;

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
  public RestHighLevelClient highLevelElasticSearchClient() {
    return new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost(
                            elasticSearchHost,
                            elasticSearchPort,
                            elasticSearchScheme
                    )
            )
    );
  }

  @Bean("customerTextViewService")
  public TextViewService<CustomerTextView> customerTextViewService(RestHighLevelClient restHighLevelClient) {
    return new TextViewService<>(restHighLevelClient, CustomerTextView.class, CustomerTextView.TYPE, CustomerTextView.INDEX);
  }

  @Bean("orderTextViewService")
  public TextViewService<OrderTextView> orderTextViewService(RestHighLevelClient restHighLevelClient) {
    return new TextViewService<>(restHighLevelClient, OrderTextView.class, OrderTextView.TYPE, OrderTextView.INDEX);
  }
}
