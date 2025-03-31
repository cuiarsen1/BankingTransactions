package com.example.bankingtransactions.dto;

import com.example.bankingtransactions.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for JSON serialization/deserialization
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDto {
    private UUID id;
    private String message;
    private UUID fromAccountId;
    private UUID toAccountId;
    private String amount;
    private TransactionType type;
    private String description;
    private LocalDateTime timestamp;
}
