package com.example.infrastructure.persistence.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class ProductPo {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Integer id;

  private String name;

  private BigDecimal price;

  private String status;

  private BigDecimal discount;
}
