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
public class ProductDto {
  private Integer id;

  private String name;

  private BigDecimal price;

  private String status;

  private Integer stock;

  private BigDecimal discount;

  private BigDecimal discountedPrice;
}
