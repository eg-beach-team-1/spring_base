package com.example.infrastructure.persistence.repository;

import com.example.common.base.JpaAndQueryDslExecutor;
import com.example.infrastructure.persistence.entity.OrderPo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface JpaOrderRepository extends JpaAndQueryDslExecutor<OrderPo, String> {
  List<OrderPo> findByCustomerId(String customerId);

  @Query(
      value =
          "SELECT * FROM customer_order WHERE id = :id AND customer_id = :customerId FOR UPDATE",
      nativeQuery = true)
  Optional<OrderPo> findByIdAndCustomerId(String id, String customerId);
}
