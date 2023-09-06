package com.example.infrastructure.client;

import static org.mapstruct.factory.Mappers.getMapper;

import com.example.domain.entity.Order;
import com.example.domain.entity.ProductDetail;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MessageDataMapper {
  MessageDataMapper MAPPER = getMapper(MessageDataMapper.class);

  @Mapping(target = "orderId", source = "id")
  @Mapping(target = "orderItems", expression = "java(toOrderItems(order.getProductDetails()))")
  MessageData toMessageDataFromOrder(Order order);

  default List<OrderItem> toOrderItems(List<ProductDetail> productDetails) {
    return productDetails.stream().map(this::toOrderItem).toList();
  }

  @Mapping(target = "productId", source = "id")
  @Mapping(target = "salePrice", expression = "java(productDetail.calculatePaidPrice())")
  OrderItem toOrderItem(ProductDetail productDetail);
}
