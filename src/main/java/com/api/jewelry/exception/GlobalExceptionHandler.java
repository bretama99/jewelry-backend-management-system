package com.api.jewelry.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

@ExceptionHandler(EntityNotFoundException.class)
public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
   ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
   return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
}

@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
   Map<String, String> errors = new HashMap<>();
   ex.getBindingResult().getAllErrors().forEach(error -> {
       String fieldName = ((FieldError) error).getField();
       String errorMessage = error.getDefaultMessage();
       errors.put(fieldName, errorMessage);
   });
   
   ValidationErrorResponse errorResponse = new ValidationErrorResponse(
           HttpStatus.BAD_REQUEST.value(),
           "Validation failed",
           errors
   );
   
   return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
}
}
