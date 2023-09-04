package com.example.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryRange implements Range {

  private String category;

  public boolean belongsTo(Product product) {
    return category.equals(product.getCategory());
  }
}
