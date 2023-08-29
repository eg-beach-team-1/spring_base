package com.example.domain.entity;

import java.math.BigDecimal;
import java.util.Map;
import lombok.Getter;

@Getter
public class QuantityCondition extends Condition {

  private final Integer quantity;

  public QuantityCondition(Integer quantity, BigDecimal discount) {
    super(discount);
    this.quantity = quantity;
  }

  public boolean isSatisfied(Map<Product, Integer> productToQuantity) {
    return true;
  }
}
