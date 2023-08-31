package com.example.domain.feign;

import org.springframework.http.ResponseEntity;

public interface OrderDataServiceFeign {
  ResponseEntity<Void> sendOrderCreationData(Message message);
}
