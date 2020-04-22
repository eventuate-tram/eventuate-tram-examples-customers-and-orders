package io.eventuate.examples.tram.ordersandcustomers.customers.web;

import io.eventuate.common.json.mapper.JSonMapper;
import io.eventuate.examples.tram.ordersandcustomers.customers.webapi.CreateCustomerRequest;
import io.eventuate.examples.tram.ordersandcustomers.customers.webapi.CreateCustomerResponse;
import io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer;
import io.eventuate.examples.tram.ordersandcustomers.customers.service.CustomerService;
import io.eventuate.tram.viewsupport.rebuild.DomainSnapshotExportService;
import io.eventuate.tram.viewsupport.rebuild.TopicPartitionOffset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerController {

  private CustomerService customerService;

  private DomainSnapshotExportService<Customer> domainSnapshotExportService;

  @Autowired
  public CustomerController(CustomerService customerService,
                            DomainSnapshotExportService<Customer> domainSnapshotExportService) {
    this.customerService = customerService;
    this.domainSnapshotExportService = domainSnapshotExportService;
  }

  @RequestMapping(value = "/customers", method = RequestMethod.POST)
  public CreateCustomerResponse createCustomer(@RequestBody CreateCustomerRequest createCustomerRequest) {
    Customer customer = customerService.createCustomer(createCustomerRequest.getName(), createCustomerRequest.getCreditLimit());
    return new CreateCustomerResponse(customer.getId());
  }

  @RequestMapping(value = "/customers/make-snapshot", method = RequestMethod.POST)
  public String makeSnapshot() {
    return JSonMapper.toJson(domainSnapshotExportService.exportSnapshots());
  }
}
