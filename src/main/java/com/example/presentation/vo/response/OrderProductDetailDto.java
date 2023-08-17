package com.example.presentation.vo.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderProductDetailDto {
  private Integer id;

  private String name;

  private BigDecimal unitPrice;

  private Integer quantity;

  private BigDecimal discount;

  private BigDecimal paidPrice;

  private BigDecimal priceDifference;
}
