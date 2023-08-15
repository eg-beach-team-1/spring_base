package com.example.domain.entity;

import static com.example.common.exception.BaseExceptionCode.INVALID_PRODUCT;

import com.example.common.exception.BusinessException;
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

  public void validateProduct() {
    if (this.status == ProductStatus.INVALID) {
      throw new BusinessException(INVALID_PRODUCT, "This product is invalid!");
    }
  }
}
