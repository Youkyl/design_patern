package com.examen.badwallet_api.payment.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PayRequest(
    @NotBlank String phone,
    @NotBlank String serviceName,
    @NotNull @Positive BigDecimal amount
) {}
