package io.eventuate.examples.tram.ordersandcustomers.endtoendtests;

import io.eventuate.examples.common.money.Money;
import io.eventuate.examples.tram.ordersandcustomers.customers.webapi.CreateCustomerRequest;
import io.eventuate.examples.tram.ordersandcustomers.customers.webapi.CreateCustomerResponse;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.events.OrderState;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.OrderInfo;
import io.eventuate.examples.tram.ordersandcustomers.orders.webapi.CreateOrderRequest;
import io.eventuate.examples.tram.ordersandcustomers.orders.webapi.CreateOrderResponse;
import io.eventuate.examples.tram.ordersandcustomers.orders.webapi.GetOrderResponse;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.CustomerView;
import io.eventuate.util.test.async.Eventually;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = CustomersAndOrdersEndToEndTestConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
class CustomersAndOrdersEndToEndTest {

  @Value("${host.name}")
  private String hostName;

  private String baseUrlOrders(String... path) {
    StringBuilder sb = new StringBuilder();
    sb.append("http://").append(hostName).append(":8081");
    Arrays.stream(path).forEach(p -> {
      sb.append('/').append(p);
    });
    return sb.toString();
  }

  private String baseUrlCustomers(String path) {
    return "http://"+hostName+":8082/" + path;
  }

  private String baseUrlOrderHistory(String path) {
    return "http://"+hostName+":8083/" + path;
  }

  @Autowired
  RestTemplate restTemplate;

  @Test
  void shouldApprove() {
    Long customerId = createCustomer("Fred", new Money("15.00"));
    Long orderId = createOrder(customerId, new Money("12.34"));
    assertOrderState(orderId, OrderState.APPROVED);
  }

  @Test
  void shouldReject() {
    Long customerId = createCustomer("Fred", new Money("15.00"));
    Long orderId = createOrder(customerId, new Money("123.34"));
    assertOrderState(orderId, OrderState.REJECTED);
  }

  @Test
  void shouldRejectForNonExistentCustomerId() {
    Long customerId = System.nanoTime();
    Long orderId = createOrder(customerId, new Money("123.34"));
    assertOrderState(orderId, OrderState.REJECTED);
  }

  @Test
  void shouldCancel() {
    Long customerId = createCustomer("Fred", new Money("15.00"));
    Long orderId = createOrder(customerId, new Money("12.34"));
    assertOrderState(orderId, OrderState.APPROVED);
    cancelOrder(orderId);
    assertOrderState(orderId, OrderState.CANCELLED);

    Eventually.eventually(120, 500, TimeUnit.MILLISECONDS, () -> {
      CustomerView customerView = getCustomerView(customerId);
      Map<Long, OrderInfo> orders = customerView.getOrders();
      assertThat(orders.get(orderId).getState(), is(OrderState.CANCELLED));
    });

  }

  @Test
  void shouldRejectApproveAndKeepOrdersInHistory() {
    Long customerId = createCustomer("John", new Money("1000"));

    Long order1Id = createOrder(customerId, new Money("100"));

    assertOrderState(order1Id, OrderState.APPROVED);

    Long order2Id = createOrder(customerId, new Money("1000"));

    assertOrderState(order2Id, OrderState.REJECTED);


    Eventually.eventually(120, 500, TimeUnit.MILLISECONDS, () -> {
      CustomerView customerView = getCustomerView(customerId);

      Map<Long, OrderInfo> orders = customerView.getOrders();

      assertEquals(2, orders.size());

      assertThat(orders.get(order1Id).getState(), is(OrderState.APPROVED));
      assertThat(orders.get(order2Id).getState(), is(OrderState.REJECTED));
    });
  }

  @Test
  void swaggerUiUrls() throws IOException {
    testSwaggerUiUrl(8081);
    testSwaggerUiUrl(8082);
    testSwaggerUiUrl(8083);
  }

  private void testSwaggerUiUrl(int port) throws IOException {
    assertUrlStatusIsOk("http://%s:%s/swagger-ui/index.html".formatted(hostName, port));
  }

  private void assertUrlStatusIsOk(String url) throws IOException {
    HttpURLConnection connection = (HttpURLConnection)new URL(url).openConnection();

    assertEquals(200, connection.getResponseCode());
  }

  private CustomerView getCustomerView(Long customerId) {
    String customerHistoryUrl = baseUrlOrderHistory("customers") + "/" + customerId;
    ResponseEntity<CustomerView> response = restTemplate.getForEntity(customerHistoryUrl, CustomerView.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());

    assertNotNull(response);

    return response.getBody();
  }


  private Long createCustomer(String name, Money credit) {
    return restTemplate.postForObject(baseUrlCustomers("customers"),
            new CreateCustomerRequest(name, credit), CreateCustomerResponse.class).getCustomerId();
  }

  private Long createOrder(Long customerId, Money orderTotal) {
    return restTemplate.postForObject(baseUrlOrders("orders"),
            new CreateOrderRequest(customerId, orderTotal), CreateOrderResponse.class).getOrderId();
  }

  private void cancelOrder(long orderId) {
    restTemplate.postForObject(baseUrlOrders("orders", Long.toString(orderId), "cancel"),
            null, GetOrderResponse.class);
  }

  private void assertOrderState(Long id, OrderState expectedState) {
    Eventually.eventually(120, 500, TimeUnit.MILLISECONDS, () -> {
      ResponseEntity<GetOrderResponse> response =
              restTemplate.getForEntity(baseUrlOrders("orders/" + id), GetOrderResponse.class);

      assertEquals(HttpStatus.OK, response.getStatusCode());

      GetOrderResponse order = response.getBody();

      assertEquals(expectedState, order.getOrderState());
    });
  }
}
