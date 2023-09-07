package com.example.application.assembler;

import static org.mapstruct.factory.Mappers.getMapper;

import com.example.domain.entity.OrderPreview;
import com.example.domain.entity.ProductDetail;
import com.example.presentation.vo.response.OrderPreviewDto;
import com.example.presentation.vo.response.OrderProductDetailDto;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderPreviewDtoMapper {
  OrderPreviewDtoMapper MAPPER = getMapper(OrderPreviewDtoMapper.class);

  @Mapping(target = "totalPrice", expression = "java(orderPreview.calculateTotalPrice())")
  @Mapping(target = "paidPrice", expression = "java(orderPreview.calculatePaidPrice())")
  @Mapping(
      target = "productDetails",
      expression = "java(mapProductDetails(orderPreview.getProductDetails()))")
  OrderPreviewDto toDto(OrderPreview orderPreview);

  default List<OrderProductDetailDto> mapProductDetails(List<ProductDetail> productDetails) {
    return productDetails.stream().map(this::toOrderProductDetailDto).collect(Collectors.toList());
  }

  @Mapping(target = "discountedPrice", expression = "java(productDetail.calculatePaidPrice())")
  @Mapping(
      target = "priceDifference",
      expression = "java(productDetail.calculatePriceDifference())")
  OrderProductDetailDto toOrderProductDetailDto(ProductDetail productDetail);
}
