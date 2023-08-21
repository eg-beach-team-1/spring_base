package com.example.application.service;

import static com.example.application.assembler.OrderListDtoMapper.MAPPER;
import static com.example.common.exception.BaseExceptionCode.INVALID_PRODUCT;
import static com.example.common.exception.BaseExceptionCode.PRODUCT_STOCK_SHORTAGE;

import com.example.common.exception.BusinessException;
import com.example.domain.entity.Order;
import com.example.domain.entity.Product;
import com.example.domain.entity.ProductDetail;
import com.example.domain.factory.OrderFactory;
import com.example.domain.repository.OrderRepository;
import com.example.domain.repository.ProductRepository;
import com.example.presentation.vo.request.OrderProductReqDto;
import com.example.presentation.vo.request.OrderReqDto;
import com.example.presentation.vo.response.OrderDto;
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

  public List<OrderDto> retrieveOrders(String customerId) {
    return orderRepository.findByCustomerId(customerId).stream().map(MAPPER::toDto).toList();
  }

  public OrderDto retrieveOrder(String orderId) {
    return MAPPER.toDto(orderRepository.findByOrderId(orderId));
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

    List<ProductDetail> productDetails = getProductDetails(products, productIdToQuantity);

    Order order = OrderFactory.buildOrder(orderReqDto.getCustomerId(), productDetails);

    return orderRepository.save(order);
  }

  private List<Product> getProducts(OrderReqDto orderReqDto) {
    List<Integer> ids =
        orderReqDto.getOrderProducts().stream().map(OrderProductReqDto::getProductId).toList();
    List<Product> products = productRepository.findAllById(ids);
    if (products.size() == ids.size()) {
      return products;
    } else {
      throw new BusinessException(INVALID_PRODUCT, "Some products cannot be found in db");
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
          if(product.getVersion().equals(productRepository.findById(product.getId()).getVersion())) {
            product.consume(productIdToQuantity.get(product.getId()));
            product.updateVersion();
            productRepository.save(product);
          } else {
            throw new BusinessException(PRODUCT_STOCK_SHORTAGE,"the stock of this product is less than the amount");
          }
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
