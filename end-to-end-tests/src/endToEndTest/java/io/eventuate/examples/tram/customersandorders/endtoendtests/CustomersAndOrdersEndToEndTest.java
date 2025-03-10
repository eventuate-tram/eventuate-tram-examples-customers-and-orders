package io.eventuate.examples.tram.customersandorders.endtoendtests;

import io.eventuate.examples.common.money.Money;
import io.eventuate.examples.tram.customersandorders.endtoendtests.proxies.customerservice.CreateCustomerRequest;
import io.eventuate.examples.tram.customersandorders.endtoendtests.proxies.customerservice.CreateCustomerResponse;
import io.eventuate.examples.tram.customersandorders.endtoendtests.proxies.customerservice.GetCustomerResponse;
import io.eventuate.examples.tram.customersandorders.endtoendtests.proxies.customerservice.GetCustomersResponse;
import io.eventuate.examples.tram.customersandorders.endtoendtests.proxies.orderhistoryservice.CustomerView;
import io.eventuate.examples.tram.customersandorders.endtoendtests.proxies.orderservice.*;
import io.eventuate.util.test.async.Eventually;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CustomersAndOrdersEndToEndTestConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class CustomersAndOrdersEndToEndTest {

    // Service
    /// Outbox/producer properties:
    ////  eventuate.tram.outbox.partitioning.outbox.tables
    ////  eventuate.tram.outbox.partitioning.message.partitions
    /// Flyway configuration (same as above)

    // CDC Polling configuration

    private static Logger logger = LoggerFactory.getLogger(CustomersAndOrdersEndToEndTest.class);

    private static final ApplicationUnderTest applicationUnderTest = ApplicationUnderTest.make();
    private final Money orderTotalUnderCreditLimit = new Money("12.34");
    private final Money orderTotalOverCreditLimit = new Money("123.40");
    private final Money creditLimit = new Money("15.00");


    @BeforeAll
    public static void startContainers() {
        applicationUnderTest.start();
    }



    private static final String CUSTOMER_NAME = "John";

    @Value("${host.name}")
    private String hostName;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void shouldGetCustomers() {
        GetCustomersResponse customers = restTemplate.getForObject(applicationUnderTest.apiGatewayBaseUrl(hostName, "customers"), GetCustomersResponse.class);
        assertNotNull(customers);
    }
    @Test
    public void shouldGetOrders() {
        GetOrdersResponse orders = restTemplate.getForObject(applicationUnderTest.apiGatewayBaseUrl(hostName, "orders"), GetOrdersResponse.class);
        assertNotNull(orders);
    }
    @Test
    public void shouldApprove() {
        CreateCustomerResponse createCustomerResponse = createCustomer();

      assertCustomerHasCreditLimit(createCustomerResponse.customerId());

      CreateOrderResponse createOrderResponse = createOrder(createCustomerResponse.customerId(), orderTotalUnderCreditLimit);

        assertOrderState(createOrderResponse.orderId(), OrderState.APPROVED, null);
    }

    @Nullable
    private CreateOrderResponse createOrder(Long customerId, Money orderTotal) {
        return restTemplate.postForObject(applicationUnderTest.apiGatewayBaseUrl(hostName, "orders"),
                new CreateOrderRequest(customerId, orderTotal), CreateOrderResponse.class);
    }

    @Nullable
    private CreateCustomerResponse createCustomer() {
        return restTemplate.postForObject(applicationUnderTest.apiGatewayBaseUrl(hostName, "customers"),
                new CreateCustomerRequest(CUSTOMER_NAME, creditLimit), CreateCustomerResponse.class);
    }

    private void assertCustomerHasCreditLimit(long id) {
        GetCustomerResponse customer = restTemplate.getForObject(applicationUnderTest.apiGatewayBaseUrl(hostName, "customers/" + id), GetCustomerResponse.class);
        assertEquals(creditLimit, customer.creditLimit());

    }

    @Test
    public void shouldRejectBecauseOfInsufficientCredit() {
        CreateCustomerResponse createCustomerResponse = createCustomer();

      CreateOrderResponse createOrderResponse = createOrder(createCustomerResponse.customerId(), orderTotalOverCreditLimit);

        assertOrderState(createOrderResponse.orderId(), OrderState.REJECTED, RejectionReason.INSUFFICIENT_CREDIT);
    }

    @Test
    public void shouldRejectBecauseOfUnknownCustomer() {

        CreateOrderResponse createOrderResponse = createOrder(Long.MAX_VALUE, new Money("123.40"));

        assertOrderState(createOrderResponse.orderId(), OrderState.REJECTED, RejectionReason.UNKNOWN_CUSTOMER);
    }

    @Test
    public void shouldSupportOrderHistory() {

        CreateCustomerResponse createCustomerResponse = createCustomer();

      CreateOrderResponse createOrderResponse = createOrder(createCustomerResponse.customerId(), orderTotalUnderCreditLimit);

        Eventually.eventually(() -> {
          CustomerView customerResponse = getOrderHistory(createCustomerResponse.customerId());

            assertEquals(creditLimit.getAmount().setScale(2), customerResponse.getCreditLimit().getAmount().setScale(2));
          assertEquals(createCustomerResponse.customerId(), customerResponse.getId());
            assertEquals(CUSTOMER_NAME, customerResponse.getName());
            assertEquals(1, customerResponse.getOrders().size());

            assertEquals(OrderState.APPROVED, customerResponse.getOrders().get(createOrderResponse.orderId()).getState());
        });
    }

    private CustomerView getOrderHistory(Long customerId) {
        ResponseEntity<CustomerView> customerResponseEntity =
                restTemplate.getForEntity(applicationUnderTest.apiGatewayBaseUrl(hostName, "customers", Long.toString(customerId), "orderhistory"),
                    CustomerView.class);

        assertEquals(HttpStatus.OK, customerResponseEntity.getStatusCode());

        return customerResponseEntity.getBody();
    }

    private void assertOrderState(Long id, OrderState expectedState, RejectionReason expectedRejectionReason) {
        Eventually.eventually(() -> {
            ResponseEntity<GetOrderResponse> getOrderResponseEntity = restTemplate.getForEntity(applicationUnderTest.apiGatewayBaseUrl(hostName, "orders/" + id), GetOrderResponse.class);
            assertEquals(HttpStatus.OK, getOrderResponseEntity.getStatusCode());
            GetOrderResponse order = getOrderResponseEntity.getBody();
            assertEquals(expectedState, order.orderState());
            assertEquals(expectedRejectionReason, order.rejectionReason());
        });
    }

    @Test
    public void shouldHandleOrderHistoryQueryForUnknownCustomer() {
      assertThrows(HttpClientErrorException.NotFound.class, () ->
        getOrderHistory(System.currentTimeMillis()));
    }

    @Test
    public void testSwaggerUiUrls() throws IOException {
        testSwaggerUiUrl("API Gateway", applicationUnderTest.getApigatewayPort());

        if (applicationUnderTest.exposesSwaggerUiForBackendServices()) {
            testSwaggerUiUrl("Customer Service", applicationUnderTest.getCustomerServicePort());
            testSwaggerUiUrl("Order Service", applicationUnderTest.getOrderServicePort());
        }
    }

    private void testSwaggerUiUrl(String message, int port) throws IOException {
        assertUrlStatusIsOk(message, "http://%s:%s/swagger-ui/index.html".formatted(hostName, port));
    }

    private void assertUrlStatusIsOk(String message, String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        if (connection.getResponseCode() != 200)
            Assertions.fail("%s: Expected 200 for %s, got %s".formatted(message, url, connection.getResponseCode()));
    }
}
