package com.invoiceapi.exceptions;

public class DuplicateBusinessException extends RuntimeException{

    public DuplicateBusinessException(String message) {
        super(message);
    }
}
