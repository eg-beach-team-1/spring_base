package com.example.presentation.vo.response;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderPreviewDto {
  private BigDecimal totalPrice;

  private BigDecimal paidPrice;

  private List<OrderProductDetailDto> productDetails;
}
