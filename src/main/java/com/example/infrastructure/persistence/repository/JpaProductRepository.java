package com.example.infrastructure.persistence.repository;

import com.example.common.base.JpaAndQueryDslExecutor;
import com.example.infrastructure.persistence.entity.ProductPo;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaProductRepository extends JpaAndQueryDslExecutor<ProductPo, Integer> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "SELECT * FROM product WHERE id IN :ids",nativeQuery = true)
    List<ProductPo> findAllById(List<Integer> ids);
}
