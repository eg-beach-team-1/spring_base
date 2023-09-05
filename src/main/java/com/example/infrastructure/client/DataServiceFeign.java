package com.example.infrastructure.client;

import com.example.common.configuration.FeignConfiguration;
import com.example.domain.port.Message;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "data-service-client", configuration = FeignConfiguration.class)
public interface DataServiceFeign {
  @PostMapping(consumes = "application/json")
  ResponseEntity<Void> sendOrderCreationMessage(@RequestBody Message message);
}
