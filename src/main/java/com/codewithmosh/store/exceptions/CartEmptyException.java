package com.codewithmosh.store.exceptions;

import lombok.NoArgsConstructor;

public class CartEmptyException extends RuntimeException{

    public CartEmptyException() {
        super("Cart is Empty");
    }
}
