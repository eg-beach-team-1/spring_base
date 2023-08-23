package com.example.infrastructure.persistence.repository.domain;

import com.example.domain.entity.Product;
import com.example.domain.repository.ProductRepository;
import com.example.infrastructure.persistence.assembler.ProductDataMapper;
import com.example.infrastructure.persistence.repository.JpaProductRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProductDomainRepository implements ProductRepository {
  private final JpaProductRepository jpaProductRepository;
  private final ProductDataMapper mapper = ProductDataMapper.mapper;

  @Override
  public List<Product> findAll() {
    return jpaProductRepository.findAll().stream().map(mapper::toDo).toList();
  }

  @Override
  public List<Product> findAllById(List<Integer> ids) {
    return jpaProductRepository.findAllById(ids).stream().map(mapper::toDo).toList();
  }

  @Override
  public void save(Product product) {
    jpaProductRepository.save(mapper.toPo(product));
  }
}
