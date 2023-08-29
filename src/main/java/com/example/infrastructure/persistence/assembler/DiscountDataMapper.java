package com.example.infrastructure.persistence.assembler;

import com.example.domain.entity.*;
import com.example.infrastructure.persistence.entity.DiscountRulePo;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class DiscountDataMapper {
  ObjectMapper objectMapper = new ObjectMapper();

  public DiscountRule toDO(DiscountRulePo discountRulePo) {
    return new DiscountRule(
        mapDiscountRulePoToRange(discountRulePo), mapDiscountRulePoToCondition(discountRulePo));
  }

  private List<Condition> mapDiscountRulePoToCondition(DiscountRulePo discountRulePo) {
    List<Condition> results = new ArrayList<>();
    try {
      List<Map<String, Object>> list =
          objectMapper.readValue(discountRulePo.getDiscountConditions(), List.class);
      list.sort(Comparator.comparing(m -> (Integer) m.get("priority")));
      list.forEach(
          item -> {
            if (item.containsKey("quantity")) {
              QuantityCondition condition =
                  new QuantityCondition(
                      (Integer) item.get("quantity"),
                      BigDecimal.valueOf((Double) item.get("discount")));
              results.add(condition);
            } else if (item.containsKey("price")) {
              PriceCondition condition = new PriceCondition(
                      BigDecimal.valueOf((Double) item.get("price")),
                      BigDecimal.valueOf((Double) item.get("discount")));
              results.add(condition);
            }
          });
      return results;
    } catch (java.io.IOException e) {
      e.printStackTrace();
    }
    return List.of();
  }

  private Range mapDiscountRulePoToRange(DiscountRulePo discountRulePo) {
    try {
      Map<String, Object> map =
          objectMapper.readValue(discountRulePo.getDiscountRange(), Map.class);
      Range range;
      if (map.containsKey("productIds")) {
        range = new ProductRange((List<String>) map.get("productIds"));
      } else if (map.containsKey("category")) {
        range = new CategoryRange((String) map.get("category"));
      } else range = new AllRange();

      return range;
    } catch (java.io.IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
