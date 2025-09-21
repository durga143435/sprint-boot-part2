package com.codewithmosh.store.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handlingValidationErrors(MethodArgumentNotValidException exception){

        Map<String, String> errorResponse = new HashMap();
        exception.getBindingResult().getFieldErrors().stream().forEach(ele -> errorResponse.put(ele.getField(), ele.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errorResponse);
    }

}
