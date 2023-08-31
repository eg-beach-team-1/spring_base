package com.example.infrastructure.persistence.port;

import com.example.domain.feign.Message;
import com.example.domain.feign.OrderDataServiceFeign;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class OrderDataServiceFeignImpl implements OrderDataServiceFeign {

  private final DataServiceClient dataServiceClient;

  @Override
  public ResponseEntity<Void> sendOrderCreationData(Message message) {
    return dataServiceClient.sendOrderCreationMessage(message);
  }
}
