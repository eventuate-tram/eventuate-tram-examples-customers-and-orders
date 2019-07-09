package io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.Money;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public class OrderHistoryViewService {

  private CustomerViewRepository customerViewRepository;
  private OrderViewRepository orderViewRepository;

  @Autowired
  public OrderHistoryViewService(CustomerViewRepository customerViewRepository, OrderViewRepository orderViewRepository, MongoTemplate mongoTemplate) {
    this.customerViewRepository = customerViewRepository;
    this.orderViewRepository = orderViewRepository;
  }

  public void createCustomer(Long customerId, String customerName, Money creditLimit) {
    customerViewRepository.addCustomer(customerId, customerName, creditLimit);
  }

  public void addOrder(Long customerId, Long orderId, Money orderTotal) {
    customerViewRepository.addOrder(customerId, orderId, orderTotal);
    orderViewRepository.addOrder(orderId, orderTotal);
  }

  public void approveOrder(Long customerId, Long orderId) {
    updateOrderState(customerId, orderId, OrderState.APPROVED);
  }

  private void updateOrderState(Long customerId, Long orderId, OrderState state) {
    customerViewRepository.updateOrderState(customerId, orderId, state);
    orderViewRepository.updateOrderState(orderId, state);
  }

  public void rejectOrder(Long customerId, Long orderId) {
    updateOrderState(customerId, orderId, OrderState.REJECTED);
  }

  public void cancelOrder(Long customerId, long orderId) {
    updateOrderState(customerId, orderId, OrderState.CANCELLED);
  }
}
