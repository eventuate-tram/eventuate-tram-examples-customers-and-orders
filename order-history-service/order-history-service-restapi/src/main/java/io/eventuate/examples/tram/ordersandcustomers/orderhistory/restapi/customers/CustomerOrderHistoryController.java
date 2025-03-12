package io.eventuate.examples.tram.ordersandcustomers.orderhistory.restapi.customers;

import io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend.CustomerView;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend.CustomerViewRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerOrderHistoryController {

  private final CustomerViewRepository customerViewRepository;

  public CustomerOrderHistoryController(CustomerViewRepository customerViewRepository) {
    this.customerViewRepository = customerViewRepository;
  }

  @GetMapping("/customers/{customerId}/orderhistory")
  public ResponseEntity<CustomerView> getCustomer(@PathVariable Long customerId) {
    return customerViewRepository
            .findById(customerId)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping("/customers/count")
  public long getCustomerCount() {
    return customerViewRepository.count();
  }
}
