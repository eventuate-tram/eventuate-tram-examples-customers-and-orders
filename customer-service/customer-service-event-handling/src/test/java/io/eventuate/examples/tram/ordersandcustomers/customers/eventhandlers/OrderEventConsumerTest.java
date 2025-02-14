package io.eventuate.examples.tram.ordersandcustomers.customers.eventhandlers;

import io.eventuate.examples.common.money.Money;
import io.eventuate.examples.tram.ordersandcustomers.customers.domain.CustomerService;
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
@AutoConfigureStubRunner(ids = "io.eventuate.examples.tram.ordersandcustomers:order-service-event-publishing",
    stubsMode = StubRunnerProperties.StubsMode.REMOTE )
class OrderEventConsumerTest {

  @Configuration
  @EnableAutoConfiguration(exclude = FlywayAutoConfiguration.class)
  @EnableEventuateTramContractVerifier
  @Import({CustomerServiceEventHandlerConfiguration.class})
  public static class TestConfiguration {
  }

  @Autowired
  private StubFinder stubFinder;

  @MockitoBean
  private CustomerService customerService;

  @Test
  public void shouldHandleOrderCreatedEvent() {
    stubFinder.trigger("customer-service-orderCreatedEvent");
    eventually(() -> {
      verify(customerService).reserveCredit(99L, 101L, new Money("123"));
    });
  }

}
