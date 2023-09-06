package com.example.domain.port;

import com.example.domain.entity.Order;
import org.springframework.http.ResponseEntity;

public interface OrderDataServiceClient {
  ResponseEntity<Void> sendOrderCreationData(Order order);
}
