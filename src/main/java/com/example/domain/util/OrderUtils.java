package com.example.domain.util;

import com.example.domain.entity.ProductDetail;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class OrderUtils {
  private OrderUtils() {}

  public static String generateOrderId() {
    return UUID.randomUUID().toString();
  }

  public static BigDecimal calculateTotalPrice(List<ProductDetail> productDetails) {
    return productDetails.stream()
        .map(
            productDetail ->
                productDetail
                    .getUnitPrice()
                    .multiply(BigDecimal.valueOf(productDetail.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public static BigDecimal calculatePaidPrice(List<ProductDetail> productDetails) {
    return productDetails.stream()
        .map(ProductDetail::calculatePaidPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
