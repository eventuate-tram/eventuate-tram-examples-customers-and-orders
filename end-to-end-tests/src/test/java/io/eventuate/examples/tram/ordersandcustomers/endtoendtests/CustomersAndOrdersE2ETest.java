package io.eventuate.examples.tram.ordersandcustomers.endtoendtests;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.Money;
import io.eventuate.examples.tram.ordersandcustomers.customers.webapi.CreateCustomerRequest;
import io.eventuate.examples.tram.ordersandcustomers.customers.webapi.CreateCustomerResponse;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderState;
import io.eventuate.examples.tram.ordersandcustomers.orders.webapi.CreateOrderRequest;
import io.eventuate.examples.tram.ordersandcustomers.orders.webapi.CreateOrderResponse;
import io.eventuate.examples.tram.ordersandcustomers.orders.webapi.GetOrderResponse;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.CustomerView;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.OrderView;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CustomersAndOrdersE2ETestConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class CustomersAndOrdersE2ETest{

  @Value("#{systemEnvironment['DOCKER_HOST_IP']}")
  private String hostName;

  private String baseUrlOrders(String path) {
    return "http://"+hostName+":8081/" + path;
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
  public void shouldApprove() throws Exception {
    CreateCustomerResponse createCustomerResponseResponse = restTemplate.postForObject(baseUrlCustomers("customers"),
            new CreateCustomerRequest("Fred", new Money("15.00")), CreateCustomerResponse.class);

    CreateOrderResponse createOrderResponse = restTemplate.postForObject(baseUrlOrders("orders"),
            new CreateOrderRequest(createCustomerResponseResponse.getCustomerId(), new Money("12.34")), CreateOrderResponse.class);

    assertOrderState(createOrderResponse.getOrderId(), OrderState.APPROVED);
  }

  @Test
  public void shouldReject() throws Exception {
    CreateCustomerResponse createCustomerResponseResponse = restTemplate.postForObject(baseUrlCustomers("customers"),
            new CreateCustomerRequest("Fred", new Money("15.00")), CreateCustomerResponse.class);

    CreateOrderResponse createOrderResponse = restTemplate.postForObject(baseUrlOrders("orders"),
            new CreateOrderRequest(createCustomerResponseResponse.getCustomerId(), new Money("123.40")), CreateOrderResponse.class);

    assertOrderState(createOrderResponse.getOrderId(), OrderState.REJECTED);
  }

  @Test
  public void shouldKeepHistory() throws Exception {
    CreateCustomerResponse createCustomerResponseResponse = restTemplate.postForObject(baseUrlCustomers("customers"),
            new CreateCustomerRequest("John", new Money("1000.00")), CreateCustomerResponse.class);

    CreateOrderResponse approvedCreateOrderResponse = restTemplate.postForObject(baseUrlOrders("orders"),
            new CreateOrderRequest(createCustomerResponseResponse.getCustomerId(), new Money("100")), CreateOrderResponse.class);

    Long approvedOrderId = approvedCreateOrderResponse.getOrderId();

    CreateOrderResponse rejectedCreateOrdeResponse = restTemplate.postForObject(baseUrlOrders("orders"),
            new CreateOrderRequest(createCustomerResponseResponse.getCustomerId(), new Money("1000")), CreateOrderResponse.class);

    Long rejectedOrderId = rejectedCreateOrdeResponse.getOrderId();

    String approvedOrderHistoryUrl = baseUrlOrderHistory("orders") + "/" + approvedCreateOrderResponse.getOrderId();
    String rejectedOrderHistoryUrl = baseUrlOrderHistory("orders") + "/" + rejectedCreateOrdeResponse.getOrderId();
    String customerHistoryUrl = baseUrlOrderHistory("customers") + "/" + createCustomerResponseResponse.getCustomerId();

    ResponseEntity<CustomerView> customerViewResponseEntity = null;
    ResponseEntity<OrderView> approvedOrderViewResponseEntity = null;
    ResponseEntity<OrderView> rejectedOrderViewResponseEntity = null;

    OrderState approvedCustomerOrderState = null;
    OrderState rejectedCustomerOrderState = null;

    for (int i = 0; i < 100; i++) {
      try {
        customerViewResponseEntity = restTemplate.getForEntity(customerHistoryUrl,
                CustomerView.class);

        approvedOrderViewResponseEntity = restTemplate.getForEntity(approvedOrderHistoryUrl,
                OrderView.class);

        rejectedOrderViewResponseEntity = restTemplate.getForEntity(rejectedOrderHistoryUrl,
                OrderView.class);

        if (approvedOrderViewResponseEntity.getBody().getState() != OrderState.APPROVED ||
                rejectedOrderViewResponseEntity.getBody().getState() != OrderState.REJECTED ||
                customerViewResponseEntity.getBody().getOrders().size() != 2) {

          Thread.sleep(400);

          continue;
        }

        approvedCustomerOrderState = customerViewResponseEntity.getBody().getOrders().get(approvedOrderId).getState();
        rejectedCustomerOrderState = customerViewResponseEntity.getBody().getOrders().get(rejectedOrderId).getState();

        if (approvedCustomerOrderState != OrderState.APPROVED || rejectedCustomerOrderState != OrderState.REJECTED) {
          Thread.sleep(400);
          continue;
        }

        break;
      } catch (Exception e) {
        Thread.sleep(400);
      }
    }

    Assert.assertNotNull(customerViewResponseEntity);
    Assert.assertNotNull(approvedOrderViewResponseEntity);
    Assert.assertNotNull(rejectedOrderViewResponseEntity);


    Assert.assertTrue(customerViewResponseEntity.getStatusCode().is2xxSuccessful());
    Assert.assertTrue(approvedOrderViewResponseEntity.getStatusCode().is2xxSuccessful());
    Assert.assertTrue(rejectedOrderViewResponseEntity.getStatusCode().is2xxSuccessful());

    Assert.assertEquals(2, customerViewResponseEntity.getBody().getOrders().size());
    Assert.assertEquals(OrderState.APPROVED, approvedCustomerOrderState);
    Assert.assertEquals(OrderState.REJECTED, rejectedCustomerOrderState);


    Assert.assertEquals(OrderState.APPROVED, approvedOrderViewResponseEntity.getBody().getState());
    Assert.assertEquals(OrderState.REJECTED, rejectedOrderViewResponseEntity.getBody().getState());
  }

  private void assertOrderState(Long id, OrderState expectedState) throws InterruptedException {
    GetOrderResponse order = null;
    for (int i = 0; i < 30; i++) {
      ResponseEntity<GetOrderResponse> getOrderResponseEntity = restTemplate.getForEntity(baseUrlOrders("orders/" + id), GetOrderResponse.class);
      Assert.assertEquals(HttpStatus.OK, getOrderResponseEntity.getStatusCode());
      order = getOrderResponseEntity.getBody();
      if (order.getOrderState() == expectedState)
        break;
      TimeUnit.MILLISECONDS.sleep(400);
    }

    Assert.assertEquals(expectedState, order.getOrderState());
  }
}
