package com.example.bankingtransactions.model;

import com.example.bankingtransactions.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private UUID id;
    private UUID fromAccountId;
    private UUID toAccountId;
    private double amount;
    private TransactionType type;
    private String description;
    private LocalDateTime timestamp;
}
