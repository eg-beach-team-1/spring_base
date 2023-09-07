package com.example.application.assembler;

import static org.mapstruct.factory.Mappers.getMapper;

import com.example.domain.entity.Order;
import com.example.domain.entity.ProductDetail;
import com.example.presentation.vo.response.OrderDto;
import com.example.presentation.vo.response.OrderProductDetailDto;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderListDtoMapper {
  OrderListDtoMapper MAPPER = getMapper(OrderListDtoMapper.class);

  @Mapping(target = "totalPrice", expression = "java(order.calculateTotalPrice())")
  @Mapping(target = "paidPrice", expression = "java(order.calculatePaidPrice())")
  @Mapping(target = "orderId", source = "id")
  @Mapping(
      target = "productDetails",
      expression = "java(mapProductDetails(order.getProductDetails()))")
  OrderDto toDto(Order order);

  default List<OrderProductDetailDto> mapProductDetails(List<ProductDetail> productDetails) {
    return productDetails.stream().map(this::toOrderProductDetailDto).collect(Collectors.toList());
  }

  @Mapping(target = "discountedPrice", expression = "java(productDetail.calculatePaidPrice())")
  @Mapping(
      target = "priceDifference",
      expression = "java(productDetail.calculatePriceDifference())")
  OrderProductDetailDto toOrderProductDetailDto(ProductDetail productDetail);
}
