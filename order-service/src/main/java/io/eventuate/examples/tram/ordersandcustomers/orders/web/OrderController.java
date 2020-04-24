package io.eventuate.examples.tram.ordersandcustomers.orders.web;

import io.eventuate.common.json.mapper.JSonMapper;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderDetails;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.OrderRepository;
import io.eventuate.examples.tram.ordersandcustomers.orders.service.OrderService;
import io.eventuate.examples.tram.ordersandcustomers.orders.webapi.CreateOrderResponse;
import io.eventuate.examples.tram.ordersandcustomers.orders.webapi.GetOrderResponse;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.Order;
import io.eventuate.examples.tram.ordersandcustomers.orders.webapi.CreateOrderRequest;
import io.eventuate.tram.viewsupport.rebuild.DomainSnapshotExportService;
import io.eventuate.tram.viewsupport.rebuild.TopicPartitionOffset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

  private OrderService orderService;
  private OrderRepository orderRepository;
  private DomainSnapshotExportService<Order> domainSnapshotExportService;

  @Autowired
  public OrderController(OrderService orderService,
                         OrderRepository orderRepository,
                         DomainSnapshotExportService<Order> domainSnapshotExportService) {

    this.orderService = orderService;
    this.orderRepository = orderRepository;
    this.domainSnapshotExportService = domainSnapshotExportService;
  }

  @RequestMapping(value = "/orders", method = RequestMethod.POST)
  public CreateOrderResponse createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
    Order order = orderService.createOrder(new OrderDetails(createOrderRequest.getCustomerId(), createOrderRequest.getOrderTotal()));
    return new CreateOrderResponse(order.getId());
  }

  @RequestMapping(value="/orders/{orderId}", method= RequestMethod.GET)
  public ResponseEntity<GetOrderResponse> getOrder(@PathVariable Long orderId) {
     return orderRepository
            .findById(orderId)
            .map(order -> makeSuccessfulResponse(order))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @RequestMapping(value="/orders/{orderId}/cancel", method= RequestMethod.POST)
  public ResponseEntity<GetOrderResponse> cancelOrder(@PathVariable Long orderId) {
     Order order = orderService.cancelOrder(orderId);
     return makeSuccessfulResponse(order);
  }

  @RequestMapping(value = "/orders/make-snapshot", method = RequestMethod.POST)
  public String makeSnapshot() {
    return JSonMapper.toJson(domainSnapshotExportService.exportSnapshots());
  }

  private ResponseEntity<GetOrderResponse> makeSuccessfulResponse(Order order) {
    return new ResponseEntity<>(new GetOrderResponse(order.getId(), order.getState()), HttpStatus.OK);
  }
}
