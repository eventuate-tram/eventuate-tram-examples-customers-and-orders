package io.eventuate.examples.tram.ordersandcustomers.orderhistory.web.customers;

import io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend.CustomerViewRepository;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.CustomerView;
import org.springframework.http.HttpStatus;
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
            .map(c -> new ResponseEntity<>(c, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @GetMapping("/customers/count")
  public long getCustomerCount() {
    return customerViewRepository.count();
  }
}
