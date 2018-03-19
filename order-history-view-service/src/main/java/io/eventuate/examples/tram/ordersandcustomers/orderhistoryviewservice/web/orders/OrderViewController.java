package io.eventuate.examples.tram.ordersandcustomers.orderhistoryviewservice.web.orders;

import io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend.OrderViewRepository;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.OrderView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderViewController {

  private OrderViewRepository orderViewRepository;

  @Autowired
  public OrderViewController(OrderViewRepository orderViewRepository) {
    this.orderViewRepository = orderViewRepository;
  }


  @RequestMapping(value="/orders/{orderId}", method= RequestMethod.GET)
  public ResponseEntity<OrderView> getOrder(@PathVariable Long orderId) {

    OrderView ov = orderViewRepository.findOne(orderId);
    if (ov == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } else {
      return new ResponseEntity<>(ov, HttpStatus.OK);
    }
  }


}
