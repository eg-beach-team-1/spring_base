package com.example.domain.entity;

import java.math.BigDecimal;
import java.util.Map;
import lombok.Getter;

@Getter
public class PriceCondition extends Condition {

  private final BigDecimal price;

  public PriceCondition(BigDecimal price, BigDecimal discount) {
    super(discount);
    this.price = price;
  }

  public boolean isSatisfied(Map<Product, Integer> productToQuantity) {
    BigDecimal productPrice =
        productToQuantity.entrySet().stream()
            .map(entry -> entry.getKey().getPrice().multiply(BigDecimal.valueOf(entry.getValue())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    return productPrice.compareTo(price) >= 0;
  }
}
