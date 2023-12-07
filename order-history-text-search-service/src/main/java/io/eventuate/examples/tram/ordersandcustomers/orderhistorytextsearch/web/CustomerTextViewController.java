package io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch.web;

import io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch.apiweb.CustomerTextView;
import io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch.repositories.CustomerTextViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/customers")
public class CustomerTextViewController {

  @Autowired
  private CustomerTextViewRepository customerTextViewService;

  @GetMapping
  public List<CustomerTextView> search(@RequestParam String search) {
    return customerTextViewService.findAllByCustomQuery(search);
  }

  @PostMapping
  public void createCustomerTextView(@RequestBody CustomerTextView customerTextView) {
    customerTextViewService.save(customerTextView);
  }
}
