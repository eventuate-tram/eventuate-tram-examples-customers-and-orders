package io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend;

import io.eventuate.examples.tram.ordersandcustomers.common.domain.Money;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.events.OrderState;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

public class OrderHistoryViewService {

  private CustomerViewRepository customerViewRepository;

  public OrderHistoryViewService(CustomerViewRepository customerViewRepository) {
    this.customerViewRepository = customerViewRepository;
  }

  @Retryable(
          value = { DuplicateKeyException.class },
          maxAttempts = 4,
          backoff = @Backoff(delay = 250))
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

  @Retryable(
          value = { DuplicateKeyException.class },
          maxAttempts = 4,
          backoff = @Backoff(delay = 250))
  public void approveOrder(Long customerId, Long orderId) {
    updateOrderState(customerId, orderId, OrderState.APPROVED);
  }

  private void updateOrderState(Long customerId, Long orderId, OrderState state) {
    customerViewRepository.updateOrderState(customerId, orderId, state);
  }

  @Retryable(
          value = { DuplicateKeyException.class },
          maxAttempts = 4,
          backoff = @Backoff(delay = 250))
  public void rejectOrder(Long customerId, Long orderId) {
    updateOrderState(customerId, orderId, OrderState.REJECTED);
  }

  @Retryable(
          value = { DuplicateKeyException.class },
          maxAttempts = 4,
          backoff = @Backoff(delay = 250))
  public void cancelOrder(Long customerId, long orderId) {
    updateOrderState(customerId, orderId, OrderState.CANCELLED);
  }
}
