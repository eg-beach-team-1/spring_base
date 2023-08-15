package com.example.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
  private String id;

  private String customerId;

  private OrderStatus status;

  private LocalDateTime createTime;

  private LocalDateTime updateTime;

  private List<ProductDetail> productDetails;

  public BigDecimal calculateTotalPrice() {
    return productDetails.stream()
        .map(
            productDetail ->
                productDetail.getPrice().multiply(BigDecimal.valueOf(productDetail.getAmount())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
