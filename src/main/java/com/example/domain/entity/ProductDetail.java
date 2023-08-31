package com.example.domain.entity;

import java.math.BigDecimal;
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
    return BigDecimal.valueOf(quantity)
        .multiply(Product.calculateDiscountedPrice(unitPrice, discount));
  }

  public BigDecimal calculatePriceDifference() {
    return unitPrice.multiply(BigDecimal.valueOf(quantity)).subtract(calculatePaidPrice());
  }
}
