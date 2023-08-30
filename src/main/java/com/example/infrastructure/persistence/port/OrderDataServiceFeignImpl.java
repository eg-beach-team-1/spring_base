package com.example.infrastructure.persistence.port;

import com.example.domain.feign.Message;
import com.example.domain.feign.OrderDataServiceFeign;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class OrderDataServiceFeignImpl implements OrderDataServiceFeign {

  private final DataServiceClient dataServiceClient;

  @Override
  public void sendOrderCreationData(Message message) {
    dataServiceClient.sendOrderCreationMessage(message);
  }
}
