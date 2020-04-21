package io.eventuate.examples.tram.ordersandcustomers.snapshottests;

import io.eventuate.examples.tram.ordersandcustomers.CustomerTextView;
import io.eventuate.examples.tram.ordersandcustomers.OrderTextView;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.Money;
import io.eventuate.examples.tram.ordersandcustomers.customers.webapi.CreateCustomerRequest;
import io.eventuate.examples.tram.ordersandcustomers.customers.webapi.CreateCustomerResponse;
import io.eventuate.examples.tram.ordersandcustomers.orders.webapi.CreateOrderRequest;
import io.eventuate.examples.tram.ordersandcustomers.orders.webapi.CreateOrderResponse;
import io.eventuate.tram.viewsupport.rebuild.TopicPartitionOffset;
import io.eventuate.util.test.async.Eventually;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SnapshotTestConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class SnapshotTest {

  @Value("localhost")
  private String hostName;

  private String baseUrlOrders(String path) {
    return "http://"+hostName+":8081/" + path;
  }

  private String baseUrlCustomers(String path) {
    return "http://"+hostName+":8082/" + path;
  }

  private String baseUrlOrderHistoryTextSearch(String path) {
    return "http://" + hostName + ":8084/" + path;
  }

  @Autowired
  RestTemplate restTemplate;

  @Test
  public void testCustomers() {
    Long id = createCustomer("john", new Money("200.00"));
    List<TopicPartitionOffset> topicPartitionOffsets = exportCustomerSnapshots();

    setTopicPartitionOffset("customerTextSearchServiceEvents", topicPartitionOffsets);
    execConsoleCommand("./gradlew", "mysqlbinlogwithorderhistorytextsearchserviceComposeUp");

    Eventually.eventually(100, 400, TimeUnit.MILLISECONDS, () -> {
      List<CustomerTextView> customerTextViews = Arrays.asList(restTemplate.getForEntity(baseUrlOrderHistoryTextSearch("customers?search=john"), CustomerTextView[].class).getBody());

      Assert.assertEquals(1, customerTextViews.size());
      Assert.assertEquals("john", customerTextViews.get(0).getName());
      Assert.assertEquals("200.00", customerTextViews.get(0).getCreditLimit());
      Assert.assertEquals(id.toString(), customerTextViews.get(0).getId());
    });
  }

  @Test
  public void testOrders() {
    Long customerId = createCustomer("jack", new Money("300.00"));
    Long orderId = createOrder(customerId, new Money("100.00"));

    List<TopicPartitionOffset> topicPartitionOffsets = exportOrderSnapshots();

    setTopicPartitionOffset("orderTextSearchServiceEvents", topicPartitionOffsets);
    execConsoleCommand("./gradlew", "mysqlbinlogwithorderhistorytextsearchserviceComposeUp");

    Eventually.eventually(100, 400, TimeUnit.MILLISECONDS, () -> {
      List<OrderTextView> orderTextViews = Arrays.asList(restTemplate.getForEntity(baseUrlOrderHistoryTextSearch("orders?search=100.00"), OrderTextView[].class).getBody());

      Assert.assertEquals(1, orderTextViews.size());
      Assert.assertEquals(orderId.toString(), orderTextViews.get(0).getId());
      Assert.assertEquals(customerId.toString(), orderTextViews.get(0).getCustomerId());
      Assert.assertEquals("100.00", orderTextViews.get(0).getOrderTotal());
    });
  }

  public void setTopicPartitionOffset(String group, List<TopicPartitionOffset> topicPartitionOffsets) {
    for (TopicPartitionOffset topicPartitionOffset : topicPartitionOffsets) {
      try {
        execConsoleCommand("sh",
                "set-consumer-group-offset.sh",
                group,
                topicPartitionOffset.getTopic(),
                String.valueOf(topicPartitionOffset.getOffset())).waitFor();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private Process execConsoleCommand(String... command) {
    try {
      ProcessBuilder processBuilder = new ProcessBuilder();
      processBuilder.directory(new File(".."));
      processBuilder.command(command);
      processBuilder.inheritIO();
      return processBuilder.start();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


  private List<TopicPartitionOffset> exportCustomerSnapshots() {
    return Arrays.asList(restTemplate.postForObject(baseUrlCustomers("customers/make-snapshot"), null,  TopicPartitionOffset[].class));
  }

  private List<TopicPartitionOffset> exportOrderSnapshots() {
    return Arrays.asList(restTemplate.postForObject(baseUrlOrders("orders/make-snapshot"), null,  TopicPartitionOffset[].class));
  }

  private Long createCustomer(String name, Money credit) {
    return restTemplate.postForObject(baseUrlCustomers("customers"),
            new CreateCustomerRequest(name, credit), CreateCustomerResponse.class).getCustomerId();
  }

  private Long createOrder(Long customerId, Money orderTotal) {
    return restTemplate.postForObject(baseUrlOrders("orders"),
            new CreateOrderRequest(customerId, orderTotal), CreateOrderResponse.class).getOrderId();
  }
}
