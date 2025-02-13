package io.eventuate.examples.tram.ordersandcustomers.customers.eventhandlers;

import io.eventuate.examples.tram.ordersandcustomers.orders.domain.OrderService;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.RejectionReason;
import io.eventuate.examples.tram.ordersandcustomers.orders.eventhandlers.OrderEventHandlersConfiguration;
import io.eventuate.tram.spring.testing.cloudcontract.EnableEventuateTramContractVerifier;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.StubFinder;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static io.eventuate.util.test.async.Eventually.eventually;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(ids = "eventuate-tram-examples-customers-and-orders.customer-service:customer-service-event-publishing",
    stubsMode = StubRunnerProperties.StubsMode.REMOTE )
class CustomerEventConsumerTest {

  @Configuration
  @EnableAutoConfiguration(exclude = FlywayAutoConfiguration.class)
  @EnableEventuateTramContractVerifier
  @Import({OrderEventHandlersConfiguration.class})
  public static class TestConfiguration {
  }

  @Autowired
  private StubFinder stubFinder;

  @MockitoBean
  private OrderService orderService;

  @Test
  public void shouldHandleCustomerCreditReservedEvent() {
    stubFinder.trigger("customerCreditReservedEvent");
    eventually(() -> {
      verify(orderService).approveOrder(102L);
    });
  }

  @Test
  public void shouldHandleCustomerCreditReservationFailedEvent() {
    stubFinder.trigger("customerCreditReservationFailedEvent");
    eventually(() -> {
      verify(orderService).rejectOrder(102L, RejectionReason.INSUFFICIENT_CREDIT);
    });
  }
  @Test
  public void shouldHandleCustomerValidationFailedEvent() {
    stubFinder.trigger("customerValidationFailedEvent");
    eventually(() -> {
      verify(orderService).rejectOrder(102L, RejectionReason.UNKNOWN_CUSTOMER);
    });
  }

}
