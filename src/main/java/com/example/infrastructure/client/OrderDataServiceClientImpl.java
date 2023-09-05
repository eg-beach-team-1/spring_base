package com.example.infrastructure.client;

import com.example.domain.port.Message;
import com.example.domain.port.OrderDataServiceClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class OrderDataServiceClientImpl implements OrderDataServiceClient {

  private final DataServiceFeign dataServiceFeign;

  @Override
  public ResponseEntity<Void> sendOrderCreationData(Message message) {
    return dataServiceFeign.sendOrderCreationMessage(message);
  }
}
