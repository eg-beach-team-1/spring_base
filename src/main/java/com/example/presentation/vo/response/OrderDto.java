package com.example.presentation.vo.response;

import com.example.common.BigDecimalSerializer;
import com.example.domain.entity.OrderStatus;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
public class OrderDto {
  private String orderId;

  private String customerId;

  @JsonSerialize(using = BigDecimalSerializer.class)
  private BigDecimal totalPrice;

  @JsonSerialize(using = BigDecimalSerializer.class)
  private BigDecimal paidPrice;

  private OrderStatus status;

  private LocalDateTime createTime;

  private List<OrderProductDetailDto> productDetails;
}
