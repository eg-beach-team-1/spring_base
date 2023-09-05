package com.example.domain.repository;

import com.example.domain.entity.DiscountRule;
import java.util.Optional;

public interface DiscountRepository {
  Optional<DiscountRule> findDiscountRule();
}
