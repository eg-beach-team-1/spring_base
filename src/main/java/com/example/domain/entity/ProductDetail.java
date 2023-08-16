package com.example.domain.entity;

import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetail {
  private Integer id;

  private String name;

  private BigDecimal price;

  private Integer amount;
}
