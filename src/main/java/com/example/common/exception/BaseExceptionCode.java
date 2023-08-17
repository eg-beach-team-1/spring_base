package com.example.common.exception;

import lombok.Getter;

@Getter
public enum BaseExceptionCode implements IExceptionCode {
  NOT_FOUND_CUSTOMER("Not found customer."),
  NOT_FOUND_PRODUCT("Not found product."),

  OUT_OF_STOCK("this product is out of stock."),
  INVALID_PRODUCT("Invalid product.");


  BaseExceptionCode(String enMsg) {
    this.enMsg = enMsg;
  }

  String enMsg;

  @Override
  public String getValue() {
    return this.name();
  }

  @Override
  public String getLangMessage() {
    return IExceptionCode.super.getLangMessage();
  }
}
