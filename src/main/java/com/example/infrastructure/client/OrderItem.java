package com.example.infrastructure.client;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
  private Integer productId;
  private Integer quantity;
  private BigDecimal salePrice;
}
