package com.example.bankingtransactions.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateAccountRequest {
    @NotBlank(message = "Account holder name is required")
    private String name;

    @NotNull
    @PositiveOrZero(message = "Initial balance must be greater than or equal to zero")
    private BigDecimal initialBalance;
}
