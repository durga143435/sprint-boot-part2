package com.codewithmosh.store.exceptions;

import com.codewithmosh.store.services.PaymentGateway;
import lombok.NoArgsConstructor;


public class PaymentException extends RuntimeException {
    public PaymentException(String message){
        super(message);
    }

    public PaymentException(){

    }

}
