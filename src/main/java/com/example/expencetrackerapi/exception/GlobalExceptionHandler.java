package com.example.expencetrackerapi.exception;

import com.example.expencetrackerapi.dto.response.ErrorResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFound(
          ResourceNotFoundException ex, WebRequest request) {

    ErrorResponse error =
            new ErrorResponse(
                    LocalDateTime.now(),
                    ex.getMessage(),
                    request.getDescription(false),
                    HttpStatus.NOT_FOUND.value());

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(InsufficientFundsException.class)
  public ResponseEntity<ErrorResponse> handleInsufficientFunds(
          InsufficientFundsException ex, WebRequest request) {

    ErrorResponse error =
            new ErrorResponse(
                    LocalDateTime.now(),
                    ex.getMessage(),
                    request.getDescription(false),
                    HttpStatus.BAD_REQUEST.value());

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationErrors(
          MethodArgumentNotValidException ex) {

    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult()
            .getFieldErrors()
            .forEach(
                    (FieldError error) -> {
                      errors.put(error.getField(), error.getDefaultMessage());
                    });

    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgument(
          IllegalArgumentException ex, WebRequest request) {

    ErrorResponse error =
            new ErrorResponse(
                    LocalDateTime.now(),
                    ex.getMessage(),
                    request.getDescription(false),
                    HttpStatus.BAD_REQUEST.value());

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGlobalException(
          Exception ex, WebRequest request) {

    ErrorResponse error =
            new ErrorResponse(
                    LocalDateTime.now(),
                    "Internal server error occurred",
                    ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value());

    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}