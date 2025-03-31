package com.example.bankingtransactions.exception;

import java.math.BigDecimal;
import java.util.UUID;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(UUID accountId, BigDecimal amount) {
        super("Insufficient funds in account " + accountId + " to withdraw amount: $" + amount);
    }
}
