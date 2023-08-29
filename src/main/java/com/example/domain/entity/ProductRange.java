package com.example.domain.entity;

import java.util.List;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProductRange implements Range {

  private List<String> productIds;

  public boolean belongsTo(Product product) {
    return true;
  }
}
