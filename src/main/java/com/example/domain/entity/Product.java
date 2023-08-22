package com.example.domain.entity;

import static com.example.common.exception.BaseExceptionCode.*;

import com.example.common.exception.BusinessException;
import java.math.BigDecimal;
import java.util.Objects;
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
    return this.price.multiply(this.discount);
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
