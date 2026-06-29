package com.examen.payment_service.facture.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FactureResponse(
    String reference,
    String walletCode,
    String serviceName,
    BigDecimal amount,
    LocalDate dueDate,
    boolean paid
) {}
