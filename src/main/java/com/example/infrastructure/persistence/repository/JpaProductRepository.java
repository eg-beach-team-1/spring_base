package com.example.infrastructure.persistence.repository;

import com.example.common.base.JpaAndQueryDslExecutor;
import com.example.infrastructure.persistence.entity.ProductPo;
import jakarta.persistence.LockModeType;
import java.util.List;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface JpaProductRepository extends JpaAndQueryDslExecutor<ProductPo, Integer> {
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query(value = "SELECT p FROM ProductPo p WHERE p.id IN :ids")
  List<ProductPo> findAllById(List<Integer> ids);
}
