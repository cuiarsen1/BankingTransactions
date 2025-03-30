package com.example.bankingtransactions.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TransferRequest {

    @NotNull(message = "Source account ID cannot be null")
    private UUID fromAccountId;

    @NotNull(message = "Destination account ID cannot be null")
    private UUID toAccountId;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;
}
