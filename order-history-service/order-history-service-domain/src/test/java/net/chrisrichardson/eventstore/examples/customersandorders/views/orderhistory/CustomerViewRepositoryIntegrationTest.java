package net.chrisrichardson.eventstore.examples.customersandorders.views.orderhistory;

import io.eventuate.examples.common.money.Money;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend.CustomerView;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend.CustomerViewRepository;
import io.eventuate.examples.tram.sagas.ordersandcustomers.testutil.testcontainers.ContainerTestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = OrderHistoryViewServiceIntegrationTestConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
class CustomerViewRepositoryIntegrationTest {

  private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0.4")
      .withReuse(ContainerTestUtil.shouldReuse());

  @DynamicPropertySource
  public static void startMongo(DynamicPropertyRegistry registry) {
    mongoDBContainer.start();
    registry.add("spring.data.mongodb.uri", () -> "mongodb://localhost:%s/customers.orders".formatted(mongoDBContainer.getFirstMappedPort()));
  }

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
