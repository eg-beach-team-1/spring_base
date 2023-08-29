package com.example.domain.entity;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
public class PriceCondition extends Condition {

  private final BigDecimal price;

  public PriceCondition(BigDecimal price, BigDecimal discount) {
    super(discount);
    this.price = price;
  }

  public boolean isSatisfied(Map<Product, Integer> productToQuantity) {
    return true;
  }
}
