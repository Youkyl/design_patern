package com.examen.badwallet_api.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.examen.badwallet_api.transaction.enums.TransactionType;

public record TransactionResponse(
    Long id,
    TransactionType type,
    BigDecimal amount,
    String paymentMethod,
    LocalDateTime createdAt
) {
    
}
