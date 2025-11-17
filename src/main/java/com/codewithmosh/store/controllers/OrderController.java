package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.ErrorDto;
import com.codewithmosh.store.dtos.OrdersResponse;
import com.codewithmosh.store.entities.Order;
import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.exceptions.NoOrdersFoundException;
import com.codewithmosh.store.exceptions.NoOrdersFoundForCurrentUserException;
import com.codewithmosh.store.services.AuthService;
import com.codewithmosh.store.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final AuthService authService;
    private final OrderService orderService;
    @GetMapping
    public List<OrdersResponse> getOrders(){
        User customer = authService.getCurrentUser();
        return orderService.getOrders(customer);
    }

    @GetMapping("/{orderId}")
    public OrdersResponse getOrderById(@PathVariable(name = "orderId") Long orderId){
       return orderService.getOrderById(orderId);
    }

    @ExceptionHandler(NoOrdersFoundException.class)
    public ResponseEntity<Void> handleNoOrdersFoundError(Exception e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDto> handleNoOrderFoundForCurrentUserError(Exception e){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDto(e.getMessage()));
    }


}
