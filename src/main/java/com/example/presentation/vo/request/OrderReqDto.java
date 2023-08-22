package com.example.presentation.vo.request;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderReqDto {
  private UUID customerId;

  private List<OrderProductReqDto> orderProducts;
}
