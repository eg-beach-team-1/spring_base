package com.example.domain.entity;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import lombok.Getter;

@Getter
public class QuantityCondition extends Condition {

  private final Integer quantity;

  public QuantityCondition(Integer quantity, BigDecimal discount) {
    super(discount);
    this.quantity = quantity;
  }

  public boolean isSatisfied(Map<Product, Integer> productToQuantity) {
    int amount = productToQuantity.values().stream()
            .mapToInt(Integer::intValue)
            .sum();
    return amount >= quantity;
  }
}
