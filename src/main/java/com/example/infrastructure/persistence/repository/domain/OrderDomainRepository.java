package com.example.infrastructure.persistence.repository.domain;

import static com.example.common.exception.BaseExceptionCode.NOT_FOUND_CUSTOMER;
import static com.example.common.exception.BaseExceptionCode.NOT_FOUND_ORDER;
import static com.example.infrastructure.persistence.assembler.OrderDataMapper.MAPPER;

import com.example.common.exception.BusinessException;
import com.example.domain.entity.Order;
import com.example.domain.repository.OrderRepository;
import com.example.infrastructure.persistence.assembler.OrderProductDetailsDataMapper;
import com.example.infrastructure.persistence.entity.OrderPo;
import com.example.infrastructure.persistence.repository.JpaOrderRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderDomainRepository implements OrderRepository {

  private final JpaOrderRepository jpaOrderRepository;
  private final OrderProductDetailsDataMapper orderProductDetailsDataMapper;

  @Override
  public List<Order> findByCustomerId(String customerId) {
    List<Order> orders =
        jpaOrderRepository.findByCustomerId(customerId).stream()
            .map(orderProductDetailsDataMapper::mapOrderPoToOrder)
            .toList();
    if (orders.isEmpty()) {
      throw new BusinessException(NOT_FOUND_CUSTOMER);
    }
    return orders;
  }

  @Override
  public Order findByOrderIdAndCustomerId(String orderId, String customerId) {
    OrderPo orderPo =
        jpaOrderRepository
            .findByIdAndCustomerId(orderId, customerId)
            .orElseThrow(() -> new BusinessException(NOT_FOUND_ORDER));
    return orderProductDetailsDataMapper.mapOrderPoToOrder(orderPo);
  }

  @Override
  public Order save(Order order) {
    OrderPo orderPo = jpaOrderRepository.save(MAPPER.toPo(order));
    return orderProductDetailsDataMapper.mapOrderPoToOrder(orderPo);
  }
}
