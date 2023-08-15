package com.example.application.assembler;

import static org.mapstruct.factory.Mappers.getMapper;

import com.example.domain.entity.Order;
import com.example.presentation.vo.response.OrderListDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderListDtoMapper {
  OrderListDtoMapper MAPPER = getMapper(OrderListDtoMapper.class);

  @Mapping(target = "totalPrice", expression = "java(order.calculateTotalPrice())")
  @Mapping(target = "orderId", source = "id")
  @Mapping(target = "id", expression = "java(1)")
  OrderListDto toDto(Order order);
}
