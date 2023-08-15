package com.example.domain.repository;

import com.example.domain.entity.Order;
import java.util.List;

public interface OrderRepository {
  List<Order> findByCustomerIdAndOrderId(String customerId, String orderId);

  String save(Order order);
}
