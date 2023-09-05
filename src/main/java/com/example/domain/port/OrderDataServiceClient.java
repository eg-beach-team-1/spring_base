package com.example.domain.port;

import org.springframework.http.ResponseEntity;

public interface OrderDataServiceClient {
  ResponseEntity<Void> sendOrderCreationData(Message message);
}
