package com.example.presentation.facade;

import com.example.application.service.ProductApplicationService;
import com.example.presentation.vo.response.ProductDto;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
  private final ProductApplicationService productApplicationService;

  @GetMapping
  public List<ProductDto> retrieveAllProducts() {
    return productApplicationService.findAll();
  }
}
