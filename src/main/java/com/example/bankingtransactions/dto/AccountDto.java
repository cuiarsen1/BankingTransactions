package com.example.bankingtransactions.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO for JSON serialization/deserialization
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private UUID id;
    private String name;
    private BigDecimal balance;
}
