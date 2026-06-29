package com.examen.payment_service.facture.dto;

import java.math.BigDecimal;
import java.util.List;

public record PayResponse(
    String message,
    BigDecimal totalPaid,
    List<String> facturesPaid
) {}
