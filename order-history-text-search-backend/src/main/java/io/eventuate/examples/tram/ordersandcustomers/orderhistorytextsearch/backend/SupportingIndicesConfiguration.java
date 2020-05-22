package io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch.backend;

import io.eventuate.tram.consumer.elasticsearch.ElasticsearchConsumerConfigurationProperties;
import io.eventuate.tram.consumer.kafka.elasticsearch.ElasticsearchOffsetStorageConfigurationProperties;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.io.IOException;

@Configuration
public class SupportingIndicesConfiguration {

  @Autowired
  private RestHighLevelClient elasticsearchClient;

  @Autowired
  private ElasticsearchConsumerConfigurationProperties elasticsearchConsumerSpringConfigurationProperties;

  @Autowired
  private ElasticsearchOffsetStorageConfigurationProperties eventuateKafkaConsumerElasticsearchSpringConfigurationProperties;

  @EventListener(ApplicationReadyEvent.class)
  void createSupportingIndices() throws IOException {
    createIndexIfNotExists(elasticsearchConsumerSpringConfigurationProperties.getReceivedMessagesIndexName());
    createIndexIfNotExists(eventuateKafkaConsumerElasticsearchSpringConfigurationProperties.getOffsetStorageIndexName());
  }

  private void createIndexIfNotExists(String offsetStorageIndexName) throws IOException {
    if (!elasticsearchClient.indices().exists(new GetIndexRequest(offsetStorageIndexName), RequestOptions.DEFAULT)) {
      elasticsearchClient.indices().create(new CreateIndexRequest(offsetStorageIndexName), RequestOptions.DEFAULT);
    }
  }
}
