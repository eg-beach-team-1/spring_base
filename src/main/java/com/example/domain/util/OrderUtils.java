package com.example.domain.util;

import java.util.UUID;

public class OrderUtils {
  private OrderUtils() {
    String exceptionMessage = "Class: " + OrderUtils.class + " should not be instantiated.";
    throw new UnsupportedOperationException(exceptionMessage);
  }

  public static String generateOrderId() {
    return UUID.randomUUID().toString();
  }
}
