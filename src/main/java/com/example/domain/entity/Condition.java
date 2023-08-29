package com.example.domain.entity;

import java.math.BigDecimal;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class Condition {
  protected BigDecimal discount;

  public abstract boolean isSatisfied(Map<Product, Integer> productToQuantity);
}
