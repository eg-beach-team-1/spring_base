package com.example.application.service;

import static com.example.application.assembler.OrderListDtoMapper.MAPPER;

import com.example.domain.entity.Order;
import com.example.domain.entity.Product;
import com.example.domain.entity.ProductDetail;
import com.example.domain.factory.OrderFactory;
import com.example.domain.repository.OrderRepository;
import com.example.domain.repository.ProductRepository;
import com.example.presentation.vo.request.OrderProductReqDto;
import com.example.presentation.vo.request.OrderReqDto;
import com.example.presentation.vo.response.OrderListDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class OrderApplicationService {
  private final ProductRepository productRepository;
  private final OrderRepository orderRepository;

  public List<OrderListDto> findOrderByCustomerIdAndOrderId(String customerId, String orderId) {
    return orderRepository.findByCustomerIdAndOrderId(customerId, orderId).stream()
        .map(MAPPER::toDto)
        .toList();
  }

  @Transactional
  public String createOrder(OrderReqDto orderReqDto) {
    List<Product> products = getProducts(orderReqDto);

    validateProductsStatus(products);

    Map<Integer, Integer> productIdToQuantity =
        orderReqDto.getOrderProducts().stream()
            .collect(
                Collectors.toMap(
                    OrderProductReqDto::getProductId, OrderProductReqDto::getQuantity));

    updateProductsStock(products, productIdToQuantity);

    List<ProductDetail> productDetails = getProductDetails(products, productIdToQuantity);

    Order order = OrderFactory.buildOrder(orderReqDto.getCustomerId(), productDetails);

    return orderRepository.save(order);
  }

  private List<Product> getProducts(OrderReqDto orderReqDto) {
    List<Integer> ids =
        orderReqDto.getOrderProducts().stream().map(OrderProductReqDto::getProductId).toList();
    return productRepository.findAllById(ids);
  }

  private void validateProductsStatus(List<Product> products) {
    for (Product product : products) {
      product.validateProduct();
    }
  }

  private void updateProductsStock(
      List<Product> products, Map<Integer, Integer> productIdToQuantity) {
    products.forEach(
        product -> {
          product.consume(productIdToQuantity.get(product.getId()));
          productRepository.save(product);
        });
  }

  private List<ProductDetail> getProductDetails(
      List<Product> products, Map<Integer, Integer> productIdToQuantity) {
    List<ProductDetail> productDetails = new ArrayList<>();

    for (Product product : products) {
      Integer quantity = productIdToQuantity.get(product.getId());
      productDetails.add(OrderFactory.buildProductDetail(product, quantity));
    }
    return productDetails;
  }
}
