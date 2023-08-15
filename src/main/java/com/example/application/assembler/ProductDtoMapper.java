package com.example.application.assembler;

import static org.mapstruct.factory.Mappers.getMapper;

import com.example.domain.entity.Product;
import com.example.presentation.vo.response.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductDtoMapper {
  ProductDtoMapper MAPPER = getMapper(ProductDtoMapper.class);

  ProductDto toDto(Product product);
}
