package com.example.domain.util;

import java.util.Random;

public class OrderIdGenerator {
  private OrderIdGenerator() {}

  public static OrderIdGenerator GenerateOrderIdGenerator() {
    return new OrderIdGenerator();
  }

  public String generateOrderId() {

    long MIN_ORDER_ID = 1000000000000000L;
    long MAX_ORDER_ID = 9999999999999999L;

    long currentTimeMillis = System.currentTimeMillis();
    Random random = new Random(currentTimeMillis);
    Long randomNumber = random.nextLong(MIN_ORDER_ID, MAX_ORDER_ID);

    return String.valueOf(randomNumber);
  }
}