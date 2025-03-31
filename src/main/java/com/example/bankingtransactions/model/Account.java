package com.example.bankingtransactions.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Account {
    private UUID id;
    private String name;
    private BigDecimal balance;
    private LocalDateTime createdAt;

    public Account() {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
    }

    public void deposit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        this.balance = this.balance.add(amount);
    }

    public boolean withdraw(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (this.balance.compareTo(amount) < 0) {
            return false; // Insufficient funds
        }
        this.balance = this.balance.subtract(amount);
        return true;
    }
}
