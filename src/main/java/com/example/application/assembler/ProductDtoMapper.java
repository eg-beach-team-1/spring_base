package com.example.application.assembler;

import static org.mapstruct.factory.Mappers.getMapper;

import com.example.domain.entity.Product;
import com.example.presentation.vo.response.ProductDto;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductDtoMapper {
  ProductDtoMapper MAPPER = getMapper(ProductDtoMapper.class);
  BigDecimal MIN_DISCOUNTED_PRICE = BigDecimal.valueOf(0.01);

  @Mapping(
      source = "product",
      target = "discountedPrice",
      qualifiedByName = "mapToFormattedDiscountedPrice")
  ProductDto toDto(Product product);

  @Named("mapToFormattedDiscountedPrice")
  default BigDecimal toDiscountedPrice(Product product) {
    BigDecimal discountedPrice = product.calculateDiscountedPrice();
    if (Objects.isNull(discountedPrice)) {
      return null;
    }

    BigDecimal price = discountedPrice.setScale(2, RoundingMode.HALF_DOWN);
    return price.compareTo(MIN_DISCOUNTED_PRICE) > 0 ? price : MIN_DISCOUNTED_PRICE;
  }
}
