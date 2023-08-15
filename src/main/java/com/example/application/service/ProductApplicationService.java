package com.example.application.service;

import com.example.application.assembler.ProductDtoMapper;
import com.example.domain.repository.ProductRepository;
import com.example.presentation.vo.response.ProductDto;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductApplicationService {
  private final ProductRepository productRepository;
  private final ProductDtoMapper mapper = ProductDtoMapper.MAPPER;

  public List<ProductDto> findAll() {
    return productRepository.findAll().stream().map(mapper::toDto).toList();
  }
}
