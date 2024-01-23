package io.eventuate.examples.tram.ordersandcustomers.customers.web;

import io.eventuate.common.json.mapper.JSonMapper;
import io.eventuate.examples.tram.ordersandcustomers.customers.webapi.CreateCustomerRequest;
import io.eventuate.examples.tram.ordersandcustomers.customers.webapi.CreateCustomerResponse;
import io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer;
import io.eventuate.examples.tram.ordersandcustomers.customers.service.CustomerService;
import io.eventuate.tram.viewsupport.rebuild.DomainSnapshotExportService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

  private CustomerService customerService;

  private DomainSnapshotExportService<Customer> domainSnapshotExportService;

  public CustomerController(CustomerService customerService,
                            DomainSnapshotExportService<Customer> domainSnapshotExportService) {
    this.customerService = customerService;
    this.domainSnapshotExportService = domainSnapshotExportService;
  }


  @PostMapping("/customers")
  public CreateCustomerResponse createCustomer(@RequestBody CreateCustomerRequest createCustomerRequest) {
    Customer customer = customerService.createCustomer(createCustomerRequest.getName(), createCustomerRequest.getCreditLimit());
    return new CreateCustomerResponse(customer.getId());
  }

  @PostMapping("/customers/make-snapshot")
  public String makeSnapshot() {
    return JSonMapper.toJson(domainSnapshotExportService.exportSnapshots());
  }
}
