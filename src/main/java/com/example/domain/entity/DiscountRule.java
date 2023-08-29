package com.example.domain.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DiscountRule {
  private Range range;
  private List<Condition> conditions;

  public Map<Product, BigDecimal> calculateDiscount(Map<Product, Integer> productToQuantity) {
    Map<Product, Integer> filteredProductMap =
        productToQuantity.entrySet().stream()
            .filter(entry -> range.belongsTo(entry.getKey()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    return conditions.stream()
        .filter(condition -> condition.isSatisfied(filteredProductMap))
        .map(Condition::getDiscount)
        .findFirst()
        .map(
            discount ->
                filteredProductMap.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> discount)))
        .orElse(Map.of());
  }
}
