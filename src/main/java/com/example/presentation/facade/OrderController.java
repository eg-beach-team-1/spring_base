package com.example.presentation.facade;

import com.example.application.service.OrderApplicationService;
import com.example.presentation.vo.request.OrderReqDto;
import com.example.presentation.vo.response.OrderListDto;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
  public List<OrderListDto> retrieveOrderList(
      @RequestParam("customerId") UUID customer_id,
      @RequestParam(value = "orderId", required = false) String orderId) {
    return orderApplicationService.findOrderByCustomerIdAndOrderId(customer_id.toString(), orderId);
  }
}
