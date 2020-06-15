package io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch.web;

import io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch.service.TextViewService;
import io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch.apiweb.CustomerTextView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/customers")
public class CustomerTextViewController {

  @Qualifier("customerTextViewService")
  @Autowired
  private TextViewService<CustomerTextView> customerTextViewService;

  @RequestMapping(method = RequestMethod.GET)
  public List<CustomerTextView> search(@RequestParam String search) {
    return customerTextViewService.search(search);
  }

  @RequestMapping(method = RequestMethod.POST)
  public void createCustomerTextView(@RequestBody CustomerTextView customerTextView) {
    customerTextViewService.index(customerTextView);
  }
}
