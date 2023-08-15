package com.example.domain.entity;

import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
  private Integer id;

  private String name;

  private BigDecimal price;

  private ProductStatus status;

  public boolean isValid() {
    return status == ProductStatus.VALID;
  }
}
