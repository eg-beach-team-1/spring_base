package com.example.common.exception;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BaseExceptionCode {
  NOT_FOUND_CUSTOMER(NOT_FOUND, "Not found customer."),

  NOT_FOUND_PRODUCT(NOT_FOUND, "Not found product."),

  NOT_FOUND_ORDER(NOT_FOUND, "Order not found."),

  OUT_OF_STOCK(UNPROCESSABLE_ENTITY, "this product is out of stock."),

  PRODUCT_STOCK_SHORTAGE(UNPROCESSABLE_ENTITY, "the stock of this product is less than the amount"),

  INVALID_PRODUCT(FORBIDDEN, "Invalid product."),

  ALREADY_CANCELED_ORDER(CONFLICT, "This order has been canceled already.");

  BaseExceptionCode(HttpStatus httpStatus, String enMsg) {
    this.httpStatus = httpStatus;
    this.enMsg = enMsg;
  }

  String enMsg;

  HttpStatus httpStatus;
}
