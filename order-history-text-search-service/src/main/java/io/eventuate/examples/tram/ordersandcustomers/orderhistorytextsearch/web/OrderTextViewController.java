package io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch.web;

import io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch.service.TextViewService;
import io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch.apiweb.OrderTextView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/orders")
public class OrderTextViewController {

  @Qualifier("orderTextViewService")
  @Autowired
  private TextViewService<OrderTextView> orderTextViewService;

  @RequestMapping(method = RequestMethod.GET)
  public List<OrderTextView> search(@RequestParam String search) {
    return orderTextViewService.search(search);
  }

  @RequestMapping(method = RequestMethod.POST)
  public void createOrderTextView(@RequestBody OrderTextView orderTextView) {
    orderTextViewService.index(orderTextView);
  }
}
