package com.example.application.service;

import static com.example.common.exception.BaseExceptionCode.NOT_FOUND_PRODUCT;

import com.example.application.assembler.OrderListDtoMapper;
import com.example.application.assembler.OrderPreviewDtoMapper;
import com.example.common.exception.BusinessException;
import com.example.domain.entity.Order;
import com.example.domain.entity.OrderPreview;
import com.example.domain.entity.Product;
import com.example.domain.entity.ProductDetail;
import com.example.domain.factory.OrderFactory;
import com.example.domain.port.OrderDataServiceClient;
import com.example.domain.repository.DiscountRepository;
import com.example.domain.repository.OrderRepository;
import com.example.domain.repository.ProductRepository;
import com.example.presentation.vo.request.OrderProductReqDto;
import com.example.presentation.vo.request.OrderReqDto;
import com.example.presentation.vo.response.OrderDto;
import com.example.presentation.vo.response.OrderPreviewDto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class OrderApplicationService {
  private final ProductRepository productRepository;
  private final OrderRepository orderRepository;
  private final DiscountRepository discountRepository;
  private final OrderDataServiceClient orderDataServiceClient;

  public List<OrderDto> retrieveOrders(String customerId) {
    return orderRepository.findByCustomerId(customerId).stream()
        .map(OrderListDtoMapper.MAPPER::toDto)
        .toList();
  }

  public OrderDto retrieveOrder(String orderId, String customerId) {
    return OrderListDtoMapper.MAPPER.toDto(
        orderRepository.findByOrderIdAndCustomerId(orderId, customerId));
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

    updateProductsInformation(products, productIdToQuantity);

    List<ProductDetail> productDetails =
        getProductDetailsForOrderCreation(products, productIdToQuantity);

    Order order = OrderFactory.buildOrder(orderReqDto.getCustomerId(), productDetails);

    Order orderSaved = orderRepository.save(order);

    orderDataServiceClient.sendOrderCreationData(orderSaved);

    return orderSaved.getId();
  }

  @Transactional
  public OrderPreviewDto previewOrder(OrderReqDto orderReqDto) {
    List<Product> products = getProducts(orderReqDto);

    validateProductsStatus(products);

    List<OrderProductReqDto> orderProducts = orderReqDto.getOrderProducts();
    Map<Product, Integer> productToQuantity =
        orderProducts.stream()
            .collect(
                Collectors.toMap(
                    orderProduct ->
                        products.stream()
                            .filter(
                                product ->
                                    Objects.equals(product.getId(), orderProduct.getProductId()))
                            .findFirst()
                            .orElse(null),
                    OrderProductReqDto::getQuantity));

    Map<Product, BigDecimal> productDiscount =
        discountRepository
            .findDiscountRule()
            .map(rule -> rule.calculateDiscount(productToQuantity))
            .orElseGet(
                () ->
                    products.stream()
                        .collect(Collectors.toMap(product -> product, product -> BigDecimal.ONE)));

    List<ProductDetail> productDetails =
        getProductDetailsForOrderPreview(productToQuantity, productDiscount);

    return OrderPreviewDtoMapper.MAPPER.toDto(new OrderPreview(productDetails));
  }

  @Transactional
  public String cancelOrder(String orderId, String customerId) {
    Order order = orderRepository.findByOrderIdAndCustomerId(orderId, customerId);
    OrderFactory.cancelOrder(order);
    List<ProductDetail> productDetails = order.getProductDetails();

    Map<Integer, Integer> productIdToQuantity =
        productDetails.stream()
            .collect(Collectors.toMap(ProductDetail::getId, ProductDetail::getQuantity));

    List<Product> products =
        productRepository.findAllById(productIdToQuantity.keySet().stream().toList());
    products.forEach(
        product -> {
          Integer amount = productIdToQuantity.get(product.getId());
          product.addStock(amount);
          productRepository.save(product);
        });

    return orderRepository.save(order).getId();
  }

  private List<Product> getProducts(OrderReqDto orderReqDto) {
    List<Integer> ids =
        orderReqDto.getOrderProducts().stream().map(OrderProductReqDto::getProductId).toList();
    List<Product> products = productRepository.findAllById(ids);
    if (products.size() == ids.size()) {
      return products;
    } else {
      throw new BusinessException(NOT_FOUND_PRODUCT);
    }
  }

  private void validateProductsStatus(List<Product> products) {
    for (Product product : products) {
      product.validateProduct();
    }
  }

  private void updateProductsInformation(
      List<Product> products, Map<Integer, Integer> productIdToQuantity) {
    products.forEach(
        product -> {
          product.consume(productIdToQuantity.get(product.getId()));
          productRepository.save(product);
        });
  }

  private List<ProductDetail> getProductDetailsForOrderCreation(
      List<Product> products, Map<Integer, Integer> productIdToQuantity) {
    List<ProductDetail> productDetails = new ArrayList<>();

    for (Product product : products) {
      Integer quantity = productIdToQuantity.get(product.getId());
      productDetails.add(OrderFactory.buildProductDetail(product, quantity));
    }
    return productDetails;
  }

  private List<ProductDetail> getProductDetailsForOrderPreview(
      Map<Product, Integer> productToQuantity, Map<Product, BigDecimal> productDiscount) {
    return productToQuantity.keySet().stream()
        .map(
            product ->
                new ProductDetail(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getCategory(),
                    productToQuantity.get(product),
                    productDiscount.get(product)))
        .toList();
  }
}
