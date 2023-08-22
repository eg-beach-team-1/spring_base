package com.example.presentation.facade;

import com.example.application.service.OrderApplicationService;
import com.example.presentation.vo.request.OrderReqDto;
import com.example.presentation.vo.response.OrderDto;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/orders")
public class OrderController {
  private final OrderApplicationService orderApplicationService;

  @PostMapping
  public String createOrder(@RequestBody OrderReqDto orderReqDto) {
    return orderApplicationService.createOrder(orderReqDto);
  }

  @GetMapping
  public List<OrderDto> retrieveOrderList(@RequestParam("customerId") UUID customerId) {
    return orderApplicationService.retrieveOrders(customerId.toString());
  }

  @GetMapping("/{orderId}")
  public OrderDto retrieveOrderDetail(@PathVariable String orderId) {
    return orderApplicationService.retrieveOrder(orderId);
  }

  @PatchMapping("/{orderId}")
  public String cancelOrder(@PathVariable String orderId, @RequestParam String customerId) {
    return orderApplicationService.cancelOrder(orderId, customerId);
  }
}
