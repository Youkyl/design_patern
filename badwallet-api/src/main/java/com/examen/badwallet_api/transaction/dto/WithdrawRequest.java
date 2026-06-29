package com.examen.badwallet_api.transaction.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record WithdrawRequest(
    @NotBlank String phone,
    @NotNull @Positive BigDecimal amount
) {}
