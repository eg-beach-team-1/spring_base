package com.example.infrastructure.client;

import com.example.domain.entity.Order;
import com.example.domain.port.OrderDataServiceClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class OrderDataServiceClientImpl implements OrderDataServiceClient {

  private final DataServiceFeign dataServiceFeign;
  private final MessageDataMapper messageDataMapper = MessageDataMapper.MAPPER;

  @Override
  public ResponseEntity<Void> sendOrderCreationData(Order order) {
    MessageData messageData = messageDataMapper.toMessageDataFromOrder(order);
    Message message = new Message(MessageType.ORDER_CREATION, messageData);
    return dataServiceFeign.sendOrderCreationMessage(message);
  }
}
