package com.example.domain.factory;

import static com.example.domain.entity.OrderStatus.CREATED;

import com.example.domain.entity.Order;
import com.example.domain.entity.Product;
import com.example.domain.entity.ProductDetail;
import com.example.domain.util.OrderUtils;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class OrderFactory {
  private OrderFactory() {}

  public static Order buildOrder(UUID customerId, List<ProductDetail> productDetails) {
    String orderId = OrderUtils.generateOrderId();
    LocalDateTime createTime = LocalDateTime.now();

    return new Order(orderId, customerId, CREATED, createTime, createTime, productDetails);
  }

  public static ProductDetail buildProductDetail(Product product, Integer quantity) {
    return new ProductDetail(
        product.getId(),
        product.getName(),
        product.getPrice(),
        product.getCategory(),
        quantity,
        product.getDiscount());
  }

  public static void cancelOrder(Order order) {
    order.cancel();
  }
}
