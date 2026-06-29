package com.examen.payment_service.facture.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record PayFacturesRequest(
    @NotBlank String walletCode,
    @NotBlank String serviceName,
    @NotEmpty List<String> factureReferences
) {}
