package com.example.presentation.vo.response;

import com.example.common.BigDecimalSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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

  @JsonSerialize(using = BigDecimalSerializer.class)
  private BigDecimal price;

  private String status;

  private Integer stock;

  private BigDecimal discount;

  @JsonSerialize(using = BigDecimalSerializer.class)
  private BigDecimal discountedPrice;
}
