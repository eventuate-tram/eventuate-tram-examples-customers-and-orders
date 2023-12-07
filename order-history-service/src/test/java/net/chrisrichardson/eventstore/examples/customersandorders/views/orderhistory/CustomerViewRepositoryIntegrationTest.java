package net.chrisrichardson.eventstore.examples.customersandorders.views.orderhistory;

import io.eventuate.examples.common.money.Money;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.CustomerView;
import org.junit.jupiter.api.Test;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend.CustomerViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = OrderHistoryViewServiceIntegrationTestConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
class CustomerViewRepositoryIntegrationTest {

  @Autowired
  private CustomerViewRepository customerViewRepository;

  @Test
  void shouldCreateAndFindCustomer() {

    Long customerId = System.nanoTime();
    Money creditLimit = new Money(2000);
    String customerName = "Fred";

    customerViewRepository.addCustomer(customerId, customerName, creditLimit);
    CustomerView customerView = customerViewRepository.findById(customerId).orElseThrow(IllegalArgumentException::new);

    assertEquals(customerId, customerView.getId());
    assertEquals(customerName, customerView.getName());
    assertEquals(0, customerView.getOrders().size());
    assertEquals(creditLimit, customerView.getCreditLimit());
  }
}
