package io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch.repositories;

import io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch.apiweb.CustomerTextView;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface CustomerTextViewRepository extends ElasticsearchRepository<CustomerTextView, String> {
    @Query("{\"match\": {\"_all\": \"?0\"}}")
    List<CustomerTextView> findAllByCustomQuery(String value);
}
