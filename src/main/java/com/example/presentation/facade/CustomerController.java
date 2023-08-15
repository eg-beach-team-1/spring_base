package com.example.presentation.facade;

import com.example.application.service.CustomerApplicationService;
import com.example.presentation.vo.response.CustomerDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/customers")
public class CustomerController {
  private final CustomerApplicationService customerApplicationService;

  @GetMapping("/{id}")
  public CustomerDto findById(@PathVariable String id) {
    return customerApplicationService.findById(id);
  }
}
