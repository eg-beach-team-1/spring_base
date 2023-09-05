package com.example.infrastructure.persistence.repository.domain;

import com.example.domain.entity.DiscountRule;
import com.example.domain.repository.DiscountRepository;
import com.example.infrastructure.persistence.assembler.DiscountDataMapper;
import com.example.infrastructure.persistence.repository.JpaDiscountRuleRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DiscountDomainRepository implements DiscountRepository {
  private final JpaDiscountRuleRepository jpaDiscountRuleRepository;

  private final DiscountDataMapper mapper;

  @Override
  public Optional<DiscountRule> findDiscountRule() {
    return jpaDiscountRuleRepository.findAll().stream().map(mapper::toDO).findFirst();
  }
}
