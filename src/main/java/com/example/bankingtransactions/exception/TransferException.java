package com.example.bankingtransactions.exception;

public class TransferException extends RuntimeException {
    public TransferException(String message) {
        super(message);
    }
}
