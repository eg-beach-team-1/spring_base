package com.example.infrastructure.persistence.repository.domain;

import com.example.domain.entity.DiscountRule;
import com.example.domain.repository.DiscountRepository;
import com.example.infrastructure.persistence.assembler.DiscountDataMapper;
import com.example.infrastructure.persistence.entity.DiscountRulePo;
import com.example.infrastructure.persistence.repository.JpaDiscountRuleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DiscountDomainRepository implements DiscountRepository {
  private final JpaDiscountRuleRepository jpaDiscountRuleRepository;

  private final DiscountDataMapper mapper;

  @Override
  public DiscountRule findDiscountRule() {
    DiscountRulePo rule = jpaDiscountRuleRepository.findAll().get(0);
    return mapper.toDO(rule);
  }
}
