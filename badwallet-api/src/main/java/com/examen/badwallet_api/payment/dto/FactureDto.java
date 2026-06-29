package com.examen.badwallet_api.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FactureDto(
    String reference,
    String walletCode,
    String serviceName,
    BigDecimal amount,
    LocalDate dueDate,
    boolean paid
) {
    
}
