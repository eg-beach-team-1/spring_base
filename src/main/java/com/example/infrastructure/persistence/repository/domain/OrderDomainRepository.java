package com.example.infrastructure.persistence.repository.domain;

import static com.example.common.exception.BaseExceptionCode.NOT_FOUND_PRODUCT;
import static com.example.common.exception.NotFoundException.notFoundException;
import static com.example.infrastructure.persistence.assembler.OrderDataMapper.MAPPER;

import com.example.common.exception.ExceptionCode;
import com.example.common.exception.NotFoundException;
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
      throw new NotFoundException(ExceptionCode.NOT_FOUND, "Not found customer.");
    }
    return orders;
  }

  @Override
  public Order findByOrderId(String orderId) {
    OrderPo orderPo =
        jpaOrderRepository
            .findById(orderId)
            .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND, "Order not found."));

    return orderProductDetailsDataMapper.mapOrderPoToOrder(orderPo);
  }

  @Override
  public Order findByOrderIdAndCustomerId(String orderId, String customerId) {
    OrderPo orderPo =
        jpaOrderRepository
            .findByIdAndCustomerId(orderId, customerId)
            .orElseThrow(notFoundException(NOT_FOUND_PRODUCT));
    return orderProductDetailsDataMapper.mapOrderPoToOrder(orderPo);
  }

  @Override
  public String save(Order order) {
    return jpaOrderRepository.save(MAPPER.toPo(order)).getId();
  }
}
