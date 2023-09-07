package com.example.domain.util;

import java.util.UUID;

public class OrderUtils {
  private OrderUtils() {}

  public static String generateOrderId() {
    return UUID.randomUUID().toString();
  }
}
