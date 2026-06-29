package com.examen.badwallet_api.payment.dto;

import java.math.BigDecimal;
import java.util.List;

public record PaymentConfirmation(
    String message,
    BigDecimal totalPaid,
    List<String> facturesPaid
) {
    
}
