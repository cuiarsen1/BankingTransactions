package com.example.bankingtransactions.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Separate DepositRequest and WithdrawRequest in case unique fields need to be added in the future
 */
@Data
public class WithdrawRequest {

    @NotNull(message = "Destination account ID cannot be null")
    private UUID toAccountId;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;
}
