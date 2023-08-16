package com.example.presentation.vo.response;

import com.example.domain.entity.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderListDto {
  private String orderId;

  private String customerId;

  private BigDecimal totalPrice;

  private OrderStatus status;

  private LocalDateTime createTime;

  private List<OrderProductDetailDto> productDetails;
}
