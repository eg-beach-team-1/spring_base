package com.example.common.exception;

import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

  @ExceptionHandler(ConversionFailedException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<String> handleConversion(RuntimeException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BusinessException.class)
  @ResponseBody
  public Map<String, String> handleBusinessException(
      BusinessException e, HttpServletResponse response) {
    Map<String, String> responseBody = new HashMap<>();
    responseBody.put("code", e.getCode().name());
    responseBody.put("message", e.getCode().enMsg);

    response.setStatus(e.getCode().httpStatus.value());

    return responseBody;
  }
}
