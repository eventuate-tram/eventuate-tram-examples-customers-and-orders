package net.chrisrichardson.eventstore.examples.customersandorders.views.orderhistory;

import io.eventuate.examples.common.money.Money;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.events.OrderState;
import org.junit.jupiter.api.Test;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.CustomerView;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend.CustomerViewRepository;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend.OrderHistoryViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = OrderHistoryViewServiceIntegrationTestConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
class OrderHistoryViewServiceIntegrationTest {

  @Autowired
  private OrderHistoryViewService orderHistoryViewService;

  @Autowired
  private CustomerViewRepository customerViewRepository;

  @Test
  void shouldCreateCustomerAndOrdersEtc() {
    Long customerId = System.nanoTime();
    Money creditLimit = new Money(2000);
    String customerName = "Fred";

    Long orderId1 = System.nanoTime();
    Money orderTotal1 = new Money(1234);
    Long orderId2 = System.nanoTime();
    Money orderTotal2 = new Money(3000);

    orderHistoryViewService.createCustomer(customerId, customerName, creditLimit);
    orderHistoryViewService.addOrder(customerId, orderId1, orderTotal1);
    orderHistoryViewService.approveOrder(customerId, orderId1);

    orderHistoryViewService.addOrder(customerId, orderId2, orderTotal2);
    orderHistoryViewService.rejectOrder(customerId, orderId2);

    CustomerView customerView = customerViewRepository
            .findById(customerId)
            .orElseThrow(IllegalArgumentException::new);


    assertEquals(2, customerView.getOrders().size());
    assertNotNull(customerView.getOrders().get(orderId1));
    assertNotNull(customerView.getOrders().get(orderId2));
    assertEquals(orderTotal1, customerView.getOrders().get(orderId1).getOrderTotal());
    assertEquals(OrderState.APPROVED, customerView.getOrders().get(orderId1).getState());

    assertNotNull(customerView.getOrders().get(orderId2));
    assertEquals(orderTotal2, customerView.getOrders().get(orderId2).getOrderTotal());
    assertEquals(OrderState.REJECTED, customerView.getOrders().get(orderId2).getState());

  }


}