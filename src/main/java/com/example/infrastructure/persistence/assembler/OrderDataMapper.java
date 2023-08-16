package com.example.infrastructure.persistence.assembler;

import static org.mapstruct.factory.Mappers.getMapper;

import com.example.domain.entity.Order;
import com.example.domain.entity.ProductDetail;
import com.example.infrastructure.persistence.entity.OrderPo;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderDataMapper {
  OrderDataMapper MAPPER = getMapper(OrderDataMapper.class);

  @Mapping(source = "productDetails", target = "productDetails")
  Order toDo(OrderPo orderPo, List<ProductDetail> productDetails);

  @Mappings({
    @Mapping(
        target = "productDetails",
        expression = "java(mapToProductDetails(order.getProductDetails()))"),
    @Mapping(target = "paidPrice", expression = "java(order.calculateTotalPrice())"),
  })
  OrderPo toPo(Order order);

  default String mapToProductDetails(List<ProductDetail> productDetails) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.writeValueAsString(productDetails);
    } catch (Exception e) {
      return null;
    }
  }
}
