package com.example.domain.entity;

import java.math.BigDecimal;
import java.util.Objects;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetail {
  private Integer id;

  private String name;

  private BigDecimal unitPrice;

  private Integer quantity;

  private BigDecimal discount;

  public BigDecimal calculatePaidPrice() {
    if (Objects.isNull(unitPrice)) {
      return null;
    }
    return unitPrice.multiply(discount);
  }

  public BigDecimal calculatePriceDifference() {
    return unitPrice
        .multiply(BigDecimal.valueOf(quantity))
        .subtract(calculatePaidPrice().multiply(BigDecimal.valueOf(quantity)));
  }
}
