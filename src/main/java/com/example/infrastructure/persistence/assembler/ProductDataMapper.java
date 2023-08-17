package com.example.infrastructure.persistence.assembler;

import static org.mapstruct.factory.Mappers.getMapper;

import com.example.domain.entity.Product;
import com.example.infrastructure.persistence.entity.ProductPo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductDataMapper {
  ProductDataMapper mapper = getMapper(ProductDataMapper.class);

  Product toDo(ProductPo productPo);

  ProductPo toPo(Product product);
}
