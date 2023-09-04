package com.example.infrastructure.persistence.port;

import com.example.common.configuration.FeignConfiguration;
import com.example.domain.feign.Message;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "data-service-client", configuration = FeignConfiguration.class)
public interface DataServiceClient {
  @PostMapping(consumes = "application/json")
  ResponseEntity<Void> sendOrderCreationMessage(@RequestBody Message message);
}
