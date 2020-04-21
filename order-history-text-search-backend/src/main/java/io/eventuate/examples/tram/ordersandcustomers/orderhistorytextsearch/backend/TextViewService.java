package io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch.backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.eventuate.examples.tram.ordersandcustomers.TextView;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TextViewService<T extends TextView> {
  private ObjectMapper objectMapper = new ObjectMapper();

  private TransportClient transportClient;
  private Class<T> textViewClass;
  private String type;
  private String index;

  public TextViewService(TransportClient transportClient, Class<T> textViewClass, String type, String index) {
    this.transportClient = transportClient;
    this.textViewClass = textViewClass;
    this.type = type;
    this.index = index;
  }

  public List<T> search(String value) {

    if (!transportClient.admin().indices().prepareExists(index).execute().actionGet().isExists()) {
      return Collections.emptyList();
    }

    SearchResponse response = transportClient.prepareSearch(index)
            .setTypes(type)
            .setQuery(QueryBuilders.termQuery("_all", value))
            .get();

    List<T> result = new ArrayList<>();

    for (SearchHit searchHit : response.getHits()) {
      try {
        result.add(objectMapper.readValue(searchHit.getSourceAsString(), textViewClass));
      }
      catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    return result;
  }

  public void index(TextView textView) {
    try {
      IndexResponse ir = transportClient
          .prepareIndex(index, type, textView.getId())
          .setSource(objectMapper.writeValueAsString(textView), XContentType.JSON)
          .get();
    }
    catch (JsonProcessingException e) {
        throw new RuntimeException(e);
    }
  }

  public void remove(String id) {
    transportClient.prepareDelete(index, type, id).get();
  }
}
