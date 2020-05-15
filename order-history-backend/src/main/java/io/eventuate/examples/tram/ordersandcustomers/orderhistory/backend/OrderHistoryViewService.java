package io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.Money;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

public class OrderHistoryViewService {

  private CustomerViewRepository customerViewRepository;

  @Autowired
  public OrderHistoryViewService(CustomerViewRepository customerViewRepository) {
    this.customerViewRepository = customerViewRepository;
  }

  public void createCustomer(Long customerId, String customerName, Money creditLimit) {
    customerViewRepository.addCustomer(customerId, customerName, creditLimit);
  }

  @Retryable(
          value = { DuplicateKeyException.class },
          maxAttempts = 4,
          backoff = @Backoff(delay = 250))
  public void addOrder(Long customerId, Long orderId, Money orderTotal) {
    customerViewRepository.addOrder(customerId, orderId, orderTotal);
  }

  public void approveOrder(Long customerId, Long orderId) {
    updateOrderState(customerId, orderId, OrderState.APPROVED);
  }

  @Retryable(
          value = { DuplicateKeyException.class },
          maxAttempts = 4,
          backoff = @Backoff(delay = 250))
  private void updateOrderState(Long customerId, Long orderId, OrderState state) {
    customerViewRepository.updateOrderState(customerId, orderId, state);
  }

  public void rejectOrder(Long customerId, Long orderId) {
    updateOrderState(customerId, orderId, OrderState.REJECTED);
  }

  public void cancelOrder(Long customerId, long orderId) {
    updateOrderState(customerId, orderId, OrderState.CANCELLED);
  }
}
