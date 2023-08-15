package com.example.application.service;

import static com.example.application.assembler.OrderListDtoMapper.MAPPER;
import static com.example.common.exception.BaseExceptionCode.INVALID_PRODUCT;
import static com.example.domain.entity.OrderStatus.CREATED;

import com.example.common.exception.BusinessException;
import com.example.domain.convertor.ProductConvertor;
import com.example.domain.entity.Order;
import com.example.domain.entity.Product;
import com.example.domain.entity.ProductDetail;
import com.example.domain.repository.OrderRepository;
import com.example.domain.repository.ProductRepository;
import com.example.domain.util.OrderUtils;
import com.example.presentation.vo.request.OrderProductReqDto;
import com.example.presentation.vo.request.OrderReqDto;
import com.example.presentation.vo.response.OrderListDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderApplicationService {
  private final ProductRepository productRepository;
  private final OrderRepository orderRepository;

  public List<OrderListDto> findOrderByCustomerIdAndOrderId(String customerId, String orderId) {
    return orderRepository.findByCustomerId(customerId).stream()
        .map(MAPPER::toDto)
        .filter(orderListDto -> orderId == null || orderId.equals(orderListDto.getOrderId()))
        .toList();
  }

  public String createOrder(OrderReqDto orderReqDto) throws JsonProcessingException {

    List<ProductDetail> productDetails = getProductDetails(orderReqDto);

    BigDecimal totalPrice = OrderUtils.calculateTotalPrice(productDetails);

    Order order =
        Order.builder()
            .customerId(orderReqDto.getCustomerId())
            .totalPrice(totalPrice)
            .status(CREATED)
            .productDetails(productDetails)
            .build();

    return orderRepository.save(order);
  }

  private List<ProductDetail> getProductDetails(OrderReqDto orderReqDto) {

    Function<OrderProductReqDto, ProductDetail> toProductDetail =
        (orderProductReqDto) -> {
          Product product = productRepository.findById(orderProductReqDto.getProductId());
          checkValidStatus(product);
          ProductDetail productDetail = ProductConvertor.toProductDetail(product);
          productDetail.setAmount(orderProductReqDto.getQuantity());
          return productDetail;
        };

    return orderReqDto.getOrderProducts().stream().map(toProductDetail).toList();
  }

  private static void checkValidStatus(Product product) {
    if (product.getStatus() == ProductStatus.INVALID) {
      throw new BusinessException(
          INVALID_PRODUCT, "Product of id [" + product.getId() + "] is invalid");
    }
  }
}
