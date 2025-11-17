package com.codewithmosh.store.exceptions;

public class NoOrdersFoundException extends RuntimeException{

    public NoOrdersFoundException(){
        super("No Orders Found");
    }
}
