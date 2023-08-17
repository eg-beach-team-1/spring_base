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
public class OrderProductDetailDto {
  private Integer id;

  private String name;

  @JsonSerialize(using = BigDecimalSerializer.class)
  private BigDecimal unitPrice;

  private Integer quantity;

  private BigDecimal discount;

  @JsonSerialize(using = BigDecimalSerializer.class)
  private BigDecimal discountedPrice;

  @JsonSerialize(using = BigDecimalSerializer.class)
  private BigDecimal priceDifference;
}
