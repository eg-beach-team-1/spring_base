package com.example.infrastructure.client;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageData {
  private String customerId;
  private String orderId;
  private List<OrderItem> orderItems;
}
