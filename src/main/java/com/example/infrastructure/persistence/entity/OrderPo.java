package com.example.infrastructure.persistence.entity;

import com.example.domain.entity.OrderStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer_order")
public class OrderPo {
  @Id
  @GeneratedValue(generator = "system-uuid")
  @GenericGenerator(name = "system-uuid", strategy = "uuid")
  private String id;

  private String customerId;

  private BigDecimal paidPrice;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  @CreationTimestamp private LocalDateTime createTime;

  @UpdateTimestamp private LocalDateTime updateTime;

  @Column(columnDefinition = "json")
  private String productDetails;
}
