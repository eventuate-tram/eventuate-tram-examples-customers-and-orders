package io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.Money;
import io.eventuate.tram.spring.consumer.common.TramNoopDuplicateMessageDetectorConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = OrderHistoryViewServiceRetryIntegrationTest.Config.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class OrderHistoryViewServiceRetryIntegrationTest {

  @Autowired
  private OrderHistoryViewService orderHistoryService;

  @MockBean
  private CustomerViewRepository customerViewRepository;

  @MockBean
  private OrderViewRepository orderViewRepository;

  @Configuration
  @EnableAutoConfiguration
  @Import({OrderHistoryViewMongoConfiguration.class, TramNoopDuplicateMessageDetectorConfiguration.class})
  public static class Config {
    @Bean
    public OrderHistoryViewService orderHistoryViewService(CustomerViewRepository customerViewRepository, OrderViewRepository orderViewRepository) {
      return new OrderHistoryViewService(customerViewRepository, orderViewRepository);
    }

  }

  @Test
  public void shouldRetry() {
    long customerId = 99L;
    long orderId = 101L;
     doThrow(new DuplicateKeyException("By test")).doNothing().when(customerViewRepository).addOrder(customerId, orderId, Money.ZERO);

    orderHistoryService.addOrder(customerId, orderId, Money.ZERO);

    verify(customerViewRepository, times(2)).addOrder(customerId, orderId, Money.ZERO);
  }

}