package com.examen.badwallet_api.transaction.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TransferRequest(
    @NotBlank String senderPhone,
    @NotBlank String receiverPhone,
    @NotNull @Positive BigDecimal amount
) {}