package com.example.domain.entity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductRange implements Range {

  private List<String> productIds;

  public boolean belongsTo(Product product) {
    return productIds.contains(String.valueOf(product.getId()));
  }
}
