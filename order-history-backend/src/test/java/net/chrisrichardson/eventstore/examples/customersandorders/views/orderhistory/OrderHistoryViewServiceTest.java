package net.chrisrichardson.eventstore.examples.customersandorders.views.orderhistory;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.MoneyDTO;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderState;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.CustomerView;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.OrderView;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend.CustomerViewRepository;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend.OrderHistoryViewService;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend.OrderViewRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = OrderHistoryViewServiceTestConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class OrderHistoryViewServiceTest {

  @Autowired
  private OrderHistoryViewService orderHistoryViewService;

  @Autowired
  private CustomerViewRepository customerViewRepository;

  @Autowired
  private OrderViewRepository orderViewRepository;

  @Test
  public void shouldCreateCustomerAndOrdersEtc() {
    Long customerId = System.nanoTime();
    MoneyDTO creditLimit = new MoneyDTO(2000);
    String customerName = "Fred";

    Long orderId1 = System.nanoTime();
    MoneyDTO orderTotal1 = new MoneyDTO(1234);
    Long orderId2 = System.nanoTime();
    MoneyDTO orderTotal2 = new MoneyDTO(3000);

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

    OrderView orderView1 = orderViewRepository.findById(orderId1).orElseThrow(IllegalArgumentException::new);
    assertEquals(orderTotal1, orderView1.getOrderTotal());
    assertEquals(OrderState.APPROVED, orderView1.getState());

    OrderView orderView2 = orderViewRepository.findById(orderId2).orElseThrow(IllegalArgumentException::new);
    assertEquals(orderTotal2, orderView2.getOrderTotal());
    assertEquals(OrderState.REJECTED, orderView2.getState());
  }


}