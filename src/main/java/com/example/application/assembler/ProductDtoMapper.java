package com.example.application.assembler;

import static org.mapstruct.factory.Mappers.getMapper;

import com.example.domain.entity.Product;
import com.example.presentation.vo.response.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductDtoMapper {
  ProductDtoMapper MAPPER = getMapper(ProductDtoMapper.class);

  @Mapping(source = "product",target = "discountedPrice", qualifiedByName = "mapToFormattedDiscountedPrice")

  ProductDto toDto(Product product);

  @Named("mapToFormattedDiscountedPrice")
  default BigDecimal toDiscountedPrice(Product product) {
    BigDecimal discountedPrice = product.calculateDiscountedPrice();
    if(Objects.isNull(discountedPrice)){
      return null;
    }
    return discountedPrice.setScale(2, RoundingMode.HALF_DOWN);
  }
}
