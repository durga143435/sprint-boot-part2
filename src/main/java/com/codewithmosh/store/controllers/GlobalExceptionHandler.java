package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.ErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handleUnReadableMessages(){
        return ResponseEntity.badRequest().body(new ErrorDto("Invalid Request"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handlingValidationErrors(MethodArgumentNotValidException exception){

        Map<String, String> errorResponse = new HashMap();
        exception.getBindingResult().getFieldErrors().stream().forEach(ele -> errorResponse.put(ele.getField(), ele.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errorResponse);
    }

}
