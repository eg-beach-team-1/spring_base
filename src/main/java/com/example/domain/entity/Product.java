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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

  public static final int SCALE = 2;

  private Integer id;

  private String name;

  private BigDecimal price;

  private ProductStatus status;

  private BigDecimal discount;

  private Integer stock;

  public void validateProduct() {
    if (this.status == ProductStatus.INVALID || this.price == null) {
      throw new BusinessException(INVALID_PRODUCT, "This product is invalid!");
    }
  }

  public BigDecimal calculateDiscountedPrice() {
    if (Objects.isNull(this.price)) {
      return null;
    }
    return this.price.multiply(this.discount).setScale(SCALE, HALF_UP);
  }

  public void consume(Integer amount) {
    validateStock(amount);
    stock -= amount;
  }

  void validateStock(Integer amount) {
    if (this.stock == 0) {
      throw new BusinessException(OUT_OF_STOCK, "This product id out of stock");
    }

    if (amount > this.stock) {
      throw new BusinessException(
          PRODUCT_STOCK_SHORTAGE, "the stock of this product is less than the amount");
    }
  }
}
