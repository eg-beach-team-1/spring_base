package com.example.application.service;

import com.example.application.assembler.CustomerDtoMapper;
import com.example.domain.repository.CustomerRepository;
import com.example.presentation.vo.response.CustomerDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerApplicationService {
  private final CustomerRepository customerRepository;
  private final CustomerDtoMapper mapper = CustomerDtoMapper.MAPPER;

  public CustomerDto findById(String id) {
    return mapper.toDto(customerRepository.findById(id));
  }
}
