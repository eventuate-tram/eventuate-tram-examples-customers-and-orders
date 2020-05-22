package io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.eventuate.examples.tram.ordersandcustomers.TextView;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TextViewService<T extends TextView> {
  private ObjectMapper objectMapper = new ObjectMapper();

  private RestHighLevelClient restHighLevelClient;
  private Class<T> textViewClass;
  private String type;
  private String index;

  public TextViewService(RestHighLevelClient restHighLevelClient, Class<T> textViewClass, String type, String index) {
    this.restHighLevelClient = restHighLevelClient;
    this.textViewClass = textViewClass;
    this.type = type;
    this.index = index;
  }

  public List<T> search(String value) {
    try {
      if (!restHighLevelClient.indices().exists(new GetIndexRequest(index), RequestOptions.DEFAULT)) {
        return Collections.emptyList();
      }

      SearchResponse response = restHighLevelClient.search(new SearchRequest()
                      .indices(index)
                      .types(type)
                      .source(new SearchSourceBuilder().query(QueryBuilders.termQuery("_all", value))),
              RequestOptions.DEFAULT);

      List<T> result = new ArrayList<>();

      for (SearchHit searchHit : response.getHits()) {
        result.add(objectMapper.readValue(searchHit.getSourceAsString(), textViewClass));
      }
      return result;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void index(TextView textView) {
    try {
      restHighLevelClient.index(
              new IndexRequest()
                      .index(index)
                      .type(type)
                      .id(textView.getId())
                      .source(objectMapper.writeValueAsString(textView), XContentType.JSON),
              RequestOptions.DEFAULT);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void remove(String id) {
    try {
      restHighLevelClient.delete(new DeleteRequest().index(index).type(type).id(id), RequestOptions.DEFAULT);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
