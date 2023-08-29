package com.example.infrastructure.persistence.repository;

import com.example.common.base.JpaAndQueryDslExecutor;
import com.example.infrastructure.persistence.entity.DiscountRulePo;

public interface JpaDiscountRuleRepository extends JpaAndQueryDslExecutor<DiscountRulePo, String> {}
