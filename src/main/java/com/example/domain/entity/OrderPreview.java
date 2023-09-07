package com.example.domain.entity;

import java.math.BigDecimal;
import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderPreview {
  private List<ProductDetail> productDetails;

  public BigDecimal calculateTotalPrice() {
    return productDetails.stream()
        .map(
            productDetail ->
                productDetail
                    .getUnitPrice()
                    .multiply(BigDecimal.valueOf(productDetail.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public BigDecimal calculatePaidPrice() {
    return productDetails.stream()
        .map(ProductDetail::calculatePaidPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
