package com.example.domain.entity;

import static com.example.common.exception.BaseExceptionCode.INVALID_PRODUCT;

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
    if (this.status == ProductStatus.INVALID) {
      throw new BusinessException(INVALID_PRODUCT, "This product is invalid!");
    }
  }

  public BigDecimal calculateDiscountedPrice() {
    if (Objects.isNull(this.price)) {
      return null;
    }
    return this.price.multiply(this.discount);
  }

  public void validateStock(Integer amount) {
    if (stock >= amount) {
      return;
    }
  }
}
