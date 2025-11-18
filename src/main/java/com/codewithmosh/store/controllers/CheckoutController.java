package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.CheckoutRequest;
import com.codewithmosh.store.dtos.CheckoutResponse;
import com.codewithmosh.store.dtos.ErrorDto;
import com.codewithmosh.store.services.WebhookRequest;
import com.codewithmosh.store.exceptions.CartEmptyException;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.PaymentException;
import com.codewithmosh.store.services.CheckoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;


    @PostMapping
    public CheckoutResponse checkout(
            @Valid @RequestBody CheckoutRequest request
    ) {
        return checkoutService.checkOut(request.getCartId());
    }

    @PostMapping("/webhook")
    public void handleWebhook(
            @RequestHeader Map<String,String> headers,
            @RequestBody String payload
    ){
          checkoutService.handleWebhookEvent(new WebhookRequest(headers, payload));
    }


   /* @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorDto> handleCartNotFoundException(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                //Map.of("error","Cart not found")
                new ErrorDto("Cart not found")
        );
    }*/

  /*  @ExceptionHandler(CartEmptyException.class)
    public ResponseEntity<ErrorDto> handleCartEmptyException(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                //Map.of("error","Cart is empty")
                new ErrorDto("Cart is empty")
        );
    }*/


    //combining 2 Exceptions into single
    @ExceptionHandler({CartEmptyException.class, CartNotFoundException.class})
    public ErrorDto handleException(Exception e) {
        return new ErrorDto(e.getMessage());
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ErrorDto> handlePaymentException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto("Checkout operation got failed"));
    }
}
