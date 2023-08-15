package com.example.infrastructure.persistence.repository.domain;

import static com.example.infrastructure.persistence.assembler.OrderDataMapper.MAPPER;

import com.example.common.exception.ExceptionCode;
import com.example.common.exception.NotFoundException;
import com.example.domain.entity.Order;
import com.example.domain.repository.OrderRepository;
import com.example.infrastructure.persistence.assembler.OrderProductDetailsDataMapper;
import com.example.infrastructure.persistence.repository.JpaOrderRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderDomainRepository implements OrderRepository {

  private final JpaOrderRepository jpaOrderRepository;
  private final OrderProductDetailsDataMapper orderProductDetailsDataMapper;

  public List<Order> findByCustomerIdAndOrderId(String customerId, String orderId) {
    List<Order> orders =
        jpaOrderRepository.findByCustomerId(customerId).stream()
            .map(orderProductDetailsDataMapper::mapOrderPoToOrder)
            .filter(order -> orderId == null || orderId.equals(order.getId()))
            .toList();
    if (orders.isEmpty()) {
      throw new NotFoundException(ExceptionCode.NOT_FOUND, "Not found customer.");
    }
    return orders;
  }

  @Override
  public String save(Order order) {
    return jpaOrderRepository.save(MAPPER.toPo(order)).getOrderId();
  }
}
