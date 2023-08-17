package com.example.domain.repository;

import com.example.domain.entity.Product;
import java.util.List;

public interface ProductRepository {
  List<Product> findAll();

  Product findById(Integer productId);

  List<Product> findAllById(List<Integer> ids);

  void save(Product product);
}
