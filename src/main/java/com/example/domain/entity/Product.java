package com.example.domain.entity;

import static com.example.common.exception.BaseExceptionCode.INVALID_PRODUCT;
import static com.example.common.exception.BaseExceptionCode.OUT_OF_STOCK;
import static com.example.common.exception.BaseExceptionCode.PRODUCT_STOCK_SHORTAGE;
import static java.math.RoundingMode.HALF_UP;

import com.example.common.exception.BusinessException;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Product {

  private static final int SCALE = 2;

  private static final BigDecimal MIN_DISCOUNTED_PRICE = BigDecimal.valueOf(0.01);

  private Integer id;

  private String name;

  private BigDecimal price;

  private ProductStatus status;

  private BigDecimal discount;

  private Integer stock;

  public void validateProduct() {
    if (this.status == ProductStatus.INVALID || this.price == null) {
      throw new BusinessException(INVALID_PRODUCT);
    }
  }

  public BigDecimal calculateDiscountedPrice() {
    return calculateDiscountedPrice(this.price, this.discount);
  }

  public static BigDecimal calculateDiscountedPrice(BigDecimal price, BigDecimal discount) {
    if (Objects.isNull(price)) {
      return null;
    }
    BigDecimal discountedPrice = price.multiply(discount).setScale(SCALE, HALF_UP);
    return discountedPrice.compareTo(MIN_DISCOUNTED_PRICE) > 0
        ? discountedPrice
        : MIN_DISCOUNTED_PRICE;
  }

  public void consume(Integer amount) {
    validateStock(amount);
    stock -= amount;
  }

  public void addStock(Integer amount) {
    stock += amount;
  }

  void validateStock(Integer amount) {
    if (this.stock == 0) {
      throw new BusinessException(OUT_OF_STOCK);
    }

    if (amount > this.stock) {
      throw new BusinessException(PRODUCT_STOCK_SHORTAGE);
    }
  }
}
