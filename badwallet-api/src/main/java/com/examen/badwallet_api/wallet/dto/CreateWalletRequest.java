package com.examen.badwallet_api.wallet.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateWalletRequest(
    @NotBlank String phone,
    @Email String email,
    @NotNull @PositiveOrZero BigDecimal initialBalance,
    String code,
    String currency
) {}
