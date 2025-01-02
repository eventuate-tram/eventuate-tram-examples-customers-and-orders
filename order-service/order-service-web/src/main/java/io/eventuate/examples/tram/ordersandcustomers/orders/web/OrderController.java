package io.eventuate.examples.tram.ordersandcustomers.orders.web;

import io.eventuate.examples.tram.ordersandcustomers.orders.domain.Order;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.OrderRepository;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.events.OrderDetails;
import io.eventuate.examples.tram.ordersandcustomers.orders.service.OrderService;
import io.eventuate.examples.tram.ordersandcustomers.orders.webapi.CreateOrderRequest;
import io.eventuate.examples.tram.ordersandcustomers.orders.webapi.CreateOrderResponse;
import io.eventuate.examples.tram.ordersandcustomers.orders.webapi.GetOrderResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

  private final OrderService orderService;
  private final OrderRepository orderRepository;

  public OrderController(OrderService orderService,
                         OrderRepository orderRepository) {

    this.orderService = orderService;
    this.orderRepository = orderRepository;
  }

  @PostMapping("/orders")
  public CreateOrderResponse createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
    Order order = orderService.createOrder(new OrderDetails(createOrderRequest.getCustomerId(), createOrderRequest.getOrderTotal()));
    return new CreateOrderResponse(order.getId());
  }

  @GetMapping("/orders/{orderId}")
  public ResponseEntity<GetOrderResponse> getOrder(@PathVariable Long orderId) {
     return orderRepository
            .findById(orderId)
            .map(this::makeSuccessfulResponse)
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PostMapping("/orders/{orderId}/cancel")
  public ResponseEntity<GetOrderResponse> cancelOrder(@PathVariable Long orderId) {
     Order order = orderService.cancelOrder(orderId);
     return makeSuccessfulResponse(order);
  }

  private ResponseEntity<GetOrderResponse> makeSuccessfulResponse(Order order) {
    return new ResponseEntity<>(new GetOrderResponse(order.getId(), order.getState()), HttpStatus.OK);
  }
}
