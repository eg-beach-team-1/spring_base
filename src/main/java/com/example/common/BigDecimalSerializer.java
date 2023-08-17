package com.example.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.math.BigDecimal;

public class BigDecimalSerializer extends JsonSerializer<BigDecimal> {

  private static final BigDecimal MIN_DISCOUNTED_PRICE = BigDecimal.valueOf(0.01);

  @Override
  public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException {
    if (value != null) {
      BigDecimal price = value.setScale(2, java.math.RoundingMode.HALF_DOWN);
      price = price.compareTo(MIN_DISCOUNTED_PRICE) > 0 ? price : MIN_DISCOUNTED_PRICE;
      gen.writeNumber(price);
    }
  }
}
