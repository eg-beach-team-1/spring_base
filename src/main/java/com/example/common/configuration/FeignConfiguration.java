package com.example.common.configuration;

import feign.Logger;
import org.springframework.context.annotation.Bean;

public class FeignConfiguration {
  @Bean
  Logger.Level feignLoggerLevel() {
    return Logger.Level.FULL;
  }
}
