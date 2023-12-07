package io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch.web;

import io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch.apiweb.OrderTextView;
import io.eventuate.examples.tram.ordersandcustomers.orderhistorytextsearch.repositories.OrderTextViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/orders")
public class OrderTextViewController {

  @Qualifier("orderTextViewService")
  @Autowired
  private OrderTextViewRepository orderTextViewService;

  @GetMapping
  public List<OrderTextView> search(@RequestParam String search) {
    return orderTextViewService.findAllByCustomQuery(search);
  }

  @PostMapping
  public void createOrderTextView(@RequestBody OrderTextView orderTextView) {
    orderTextViewService.save(orderTextView);
  }
}
