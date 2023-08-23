package com.example.infrastructure.persistence.repository.domain;

import static com.example.common.exception.BaseExceptionCode.NOT_FOUND_CUSTOMER;

import com.example.common.exception.BusinessException;
import com.example.domain.entity.Customer;
import com.example.domain.repository.CustomerRepository;
import com.example.infrastructure.persistence.assembler.CustomerDataMapper;
import com.example.infrastructure.persistence.repository.JpaCustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomerDomainRepository implements CustomerRepository {
  private final JpaCustomerRepository jpaCustomerRepository;
  private final CustomerDataMapper mapper = CustomerDataMapper.mapper;

  @Override
  public Customer findById(String id) {
    return mapper.toDo(
        jpaCustomerRepository
            .findById(id)
            .orElseThrow(() -> new BusinessException(NOT_FOUND_CUSTOMER)));
  }
}
