package com.example.presentation.vo.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderProductReqDto {
  private Integer productId;

  private Long quantity;
}
