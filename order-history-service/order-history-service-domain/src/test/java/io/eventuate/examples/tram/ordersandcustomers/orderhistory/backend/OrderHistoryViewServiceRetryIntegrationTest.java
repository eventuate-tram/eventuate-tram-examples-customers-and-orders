package io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend;

import io.eventuate.examples.common.money.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = OrderHistoryViewServiceRetryIntegrationTest.Config.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
class OrderHistoryViewServiceRetryIntegrationTest {

  @Autowired
  private OrderHistoryViewService orderHistoryService;

  @MockitoBean
  private CustomerViewRepository customerViewRepository;

  @Configuration
  @EnableAutoConfiguration
  @Import({OrderHistoryViewMongoConfiguration.class})
  public static class Config {
    @Bean
    public OrderHistoryViewService orderHistoryViewService(CustomerViewRepository customerViewRepository) {
      return new OrderHistoryViewService(customerViewRepository);
    }

  }

  @Test
  void shouldRetry() {
    long customerId = 99L;
    long orderId = 101L;
     doThrow(new DuplicateKeyException("By test")).doNothing().when(customerViewRepository).addOrder(customerId, orderId, Money.ZERO);

    orderHistoryService.addOrder(customerId, orderId, Money.ZERO);

    verify(customerViewRepository, times(2)).addOrder(customerId, orderId, Money.ZERO);
  }

}
