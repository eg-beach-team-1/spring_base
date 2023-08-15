package com.example.domain.util;

import com.example.domain.entity.ProductDetail;
import java.math.BigDecimal;
import java.util.List;
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
