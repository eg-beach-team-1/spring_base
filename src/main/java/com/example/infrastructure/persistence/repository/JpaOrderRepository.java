package com.example.infrastructure.persistence.repository;

import com.example.common.base.JpaAndQueryDslExecutor;
import com.example.infrastructure.persistence.entity.OrderPo;
import java.util.List;
import java.util.Optional;

public interface JpaOrderRepository extends JpaAndQueryDslExecutor<OrderPo, String> {
  List<OrderPo> findByCustomerId(String customerId);

  Optional<OrderPo> findByIdAndCustomerId(String id, String customerId);
}
