package com.example.domain.entity;

import static com.example.common.exception.BaseExceptionCode.ALREADY_CANCELED_ORDER;
import static com.example.domain.entity.OrderStatus.CANCELED;

import com.example.common.exception.BusinessException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
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

  private UUID customerId;

  private OrderStatus status;

  private LocalDateTime createTime;

  private LocalDateTime updateTime;

  private List<ProductDetail> productDetails;

  public void cancel() {
    validateStatus();
    status = CANCELED;
  }

  private void validateStatus() {
    if (this.status == CANCELED) {
      throw new BusinessException(ALREADY_CANCELED_ORDER);
    }
  }

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
