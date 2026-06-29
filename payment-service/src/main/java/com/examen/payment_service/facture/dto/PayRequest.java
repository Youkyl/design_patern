package com.examen.payment_service.facture.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PayRequest(
    @NotBlank String walletCode,
    @NotBlank String serviceName,
    @NotNull @Positive BigDecimal amount
) {}
