package com.example.domain.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CategoryRange implements Range {

  private String category;

  public boolean belongsTo(Product product) {
    return true;
  }
}
