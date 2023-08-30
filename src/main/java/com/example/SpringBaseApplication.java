package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SpringBaseApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringBaseApplication.class, args);
  }
}
