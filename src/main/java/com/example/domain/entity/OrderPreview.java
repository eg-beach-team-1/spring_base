package com.example.domain.entity;

import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderPreview {
  private List<ProductDetail> productDetails;
}
