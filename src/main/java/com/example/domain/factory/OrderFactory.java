package com.example.domain.factory;

import com.example.domain.entity.Order;
import com.example.domain.entity.OrderStatus;
import com.example.domain.entity.Product;
import com.example.domain.entity.ProductDetail;
import com.example.domain.util.OrderUtils;
import java.time.LocalDateTime;
import java.util.List;

public class OrderFactory {
  public static Order buildOrder(
      String customerId, OrderStatus status, List<ProductDetail> productDetails) {
    String orderId = OrderUtils.generateOrderId();
    LocalDateTime createTime = LocalDateTime.now();
    return new Order(orderId, customerId, status, createTime, createTime, productDetails);
  }

  public static ProductDetail buildProductDetail(Product product, Long quantity) {
    return new ProductDetail(product.getId(), product.getName(), product.getPrice(), quantity);
  }

}
