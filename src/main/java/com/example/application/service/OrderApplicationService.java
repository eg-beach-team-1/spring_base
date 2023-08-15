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

  public String createOrder(OrderReqDto orderReqDto) {
    List<Product> products = getProducts(orderReqDto);

    validateProducts(products);

    Map<Integer, Long> productIdToQuantity =
        orderReqDto.getOrderProducts().stream()
            .collect(
                Collectors.toMap(
                    OrderProductReqDto::getProductId, OrderProductReqDto::getQuantity));

    List<ProductDetail> productDetails = getProductDetails(products, productIdToQuantity);

    Order order = OrderFactory.buildOrder(orderReqDto.getCustomerId(), productDetails);

    return orderRepository.save(order);
  }

  private List<Product> getProducts(OrderReqDto orderReqDto) {
    List<Integer> ids =
        orderReqDto.getOrderProducts().stream().map(OrderProductReqDto::getProductId).toList();
    return productRepository.findAllById(ids);
  }

  private void validateProducts(List<Product> products) {
    for (Product product : products) {
      product.validateProduct();
    }
  }

  private static List<ProductDetail> getProductDetails(
      List<Product> products, Map<Integer, Long> productIdToQuantity) {
    List<ProductDetail> productDetails = new ArrayList<>();

    for (Product product : products) {
      Long quantity = productIdToQuantity.get(product.getId());
      productDetails.add(OrderFactory.buildProductDetail(product, quantity));
    }
    return productDetails;
  }
}
