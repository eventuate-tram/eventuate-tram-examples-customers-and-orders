package io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.eventuate.examples.tram.ordersandcustomers.CustomerTextView;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CustomerTextViewService {
  @Autowired
  private TransportClient transportClient;

  private ObjectMapper objectMapper = new ObjectMapper();

  public List<CustomerTextView> search(String value) {

    if (!transportClient.admin().indices().prepareExists(CustomerTextView.INDEX).execute().actionGet().isExists()) {
      return Collections.emptyList();
    }

    SearchResponse response = transportClient.prepareSearch(CustomerTextView.INDEX)
            .setTypes(CustomerTextView.TYPE)
            .setQuery(QueryBuilders.termQuery("_all", value))
            .get();

    List<CustomerTextView> result = new ArrayList<>();

    for (SearchHit searchHit : response.getHits()) {
      try {
        result.add(objectMapper.readValue(searchHit.getSourceAsString(), CustomerTextView.class));
      }
      catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    return result;
  }

  public void index(CustomerTextView customerTextView) {
    try {
      IndexResponse ir = transportClient
          .prepareIndex(CustomerTextView.INDEX, CustomerTextView.TYPE, customerTextView.getId())
          .setSource(objectMapper.writeValueAsString(customerTextView), XContentType.JSON)
          .get();
    }
    catch (JsonProcessingException e) {
        throw new RuntimeException(e);
    }
  }

  public void remove(String id) {
    transportClient.prepareDelete(CustomerTextView.INDEX, CustomerTextView.TYPE, id).get();
  }
}
