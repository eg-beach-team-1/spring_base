package com.example.common.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

  private final BaseExceptionCode code;

  public BusinessException(BaseExceptionCode code) {
    this.code = code;
  }
}
