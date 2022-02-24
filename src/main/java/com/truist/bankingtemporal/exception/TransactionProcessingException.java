package com.truist.bankingtemporal.exception;

public class TransactionProcessingException extends RuntimeException{
    private final String message;

    public TransactionProcessingException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
