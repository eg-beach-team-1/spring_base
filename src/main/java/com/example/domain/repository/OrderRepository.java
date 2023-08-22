package com.example.domain.repository;

import com.example.domain.entity.Order;
import java.util.List;

public interface OrderRepository {
  List<Order> findByCustomerId(String customerId);

  Order findByOrderId(String orderId);

  Order findByOrderIdAndCustomerId(String orderId, String customerId);

  String save(Order order);
}
