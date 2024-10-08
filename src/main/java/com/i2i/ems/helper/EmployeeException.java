package com.i2i.ems.helper;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class EmployeeException extends RuntimeException {
  public EmployeeException(String message, Throwable cause) {
    super(message, cause);
  }
}