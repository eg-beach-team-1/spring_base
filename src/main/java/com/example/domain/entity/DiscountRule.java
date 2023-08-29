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

  public Map<Integer, BigDecimal> calculateDiscount(Map<Integer, Integer> productIdToQuantity) {
    Map<Integer, Integer> filteredProductMap =
        productIdToQuantity.entrySet().stream()
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
