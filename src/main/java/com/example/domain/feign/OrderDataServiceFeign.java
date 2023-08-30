package com.example.domain.feign;

public interface OrderDataServiceFeign {
  void sendOrderCreationData(Message message);
}
