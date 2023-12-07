package io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch.repositories;

import io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch.apiweb.OrderTextView;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface OrderTextViewRepository extends ElasticsearchRepository<OrderTextView, String> {
    @Query("{\"match\": {\"_all\": \"?0\"}}")
    List<OrderTextView> findAllByCustomQuery(String value);
}
