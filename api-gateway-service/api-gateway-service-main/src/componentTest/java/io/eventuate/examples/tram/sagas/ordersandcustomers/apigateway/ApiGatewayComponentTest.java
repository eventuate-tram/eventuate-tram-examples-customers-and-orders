package io.eventuate.examples.tram.sagas.ordersandcustomers.apigateway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.reactive.function.client.WebClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiGatewayComponentTest {

  @Configuration
  @EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
  static public class Config {
  }

  @LocalServerPort
  private long port;


  private static WireMockServer customerService;
  private static WireMockServer orderService;
  private static WireMockServer orderHistoryService;

  @DynamicPropertySource
  static void registerWireMockProperties(DynamicPropertyRegistry registry) {
    customerService = WireMockUtil.mockOnDynamicPort();
    orderService = WireMockUtil.mockOnDynamicPort();
    orderHistoryService = WireMockUtil.mockOnDynamicPort();

    customerService.start();
    orderService.start();
    orderHistoryService.start();

    registry.add("customer.destinations.customerServiceUrl", WireMockUtil.makeServiceUrlSupplier(customerService));
    registry.add("order.destinations.orderServiceUrl", WireMockUtil.makeServiceUrlSupplier(orderService));
    registry.add("orderhistory.destinations.orderHistoryServiceUrl", WireMockUtil.makeServiceUrlSupplier(orderHistoryService));
  }

  private WebClient webClient;

  @BeforeEach
  public void setUp() {
    webClient = WebClient.builder().baseUrl("http://localhost:%s".formatted(port)).build();
  }

  private String get(String path) {
    return webClient.get().uri(path).retrieve().bodyToMono(String.class).block();
  }


  @Test
  public void shouldGetOrder() {

    var expectedResponse = "{}";

    orderService.stubFor(WireMock.get(urlEqualTo("/orders/101"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(expectedResponse)));

    String response = get("/orders/101");

    assertEquals(expectedResponse, response);

    orderService.verify(getRequestedFor(urlMatching("/orders/101")));
  }

  @Test
  public void shouldGetOrders() {

    var expectedResponse = "{}";

    orderService.stubFor(WireMock.get(urlEqualTo("/orders"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(expectedResponse)));

    String response = get("/orders");

    assertEquals(expectedResponse, response);

    orderService.verify(getRequestedFor(urlMatching("/orders")));
  }

  @Test
  public void shouldGetOrderHistory() throws JSONException, JsonProcessingException {


    var ordersJSon = new JSONArray();
    ordersJSon.put(new JSONObject().put("orderId", 1).put("orderState", "APPROVED").put("rejectionReason", JSONObject.NULL));


    orderHistoryService.stubFor(WireMock.get(urlEqualTo("/customers/5/orderhistory"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(ordersJSon.toString())));


    var mapper = new ObjectMapper();

    var response = get("/customers/5/orderhistory");

    assertEquals(mapper.readTree(ordersJSon.toString()), mapper.readTree(response));

    orderHistoryService.verify(getRequestedFor(urlMatching("/customers/5/orderhistory")));
  }

}