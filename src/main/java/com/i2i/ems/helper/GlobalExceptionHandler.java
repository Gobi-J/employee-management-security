package com.i2i.ems.helper;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
  public ResponseEntity<String> handleIllegalArgument(RuntimeException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = {NoSuchElementException.class})
  public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = {DuplicateKeyException.class})
  public ResponseEntity<String> handleDuplicateKeyException(DuplicateKeyException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(value = {MethodArgumentNotValidException.class})
  public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    Map<String, String> errors = new HashMap<>();
    for(FieldError fieldError : e.getBindingResult().getFieldErrors()) {
      errors.put(fieldError.getField(), fieldError.getDefaultMessage());
    }
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = {UnAuthorizedException.class, UsernameNotFoundException.class})
  public ResponseEntity<String> handleUserServiceException(Exception e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
//    if (e instanceof BadCredentialsException) {
//      return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
//    } else if (e instanceof AccessDeniedException) {
//      return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage());
//    } else if (e instanceof SignatureException) {
//      return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
//    } else if (e instanceof ExpiredJwtException) {
//      return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
//    } else {
//      return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
//    }
  }

  @ExceptionHandler(value = {EmployeeException.class})
  public ResponseEntity<String> handleException(EmployeeException e) {
    return new ResponseEntity<>("Error occurred with the server\n" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}