package com.examen.badwallet_api.payment.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record PayFacturesRequest(
    @NotBlank String phone,
    @NotBlank String serviceName,
    @NotEmpty List<String> factureReferences
) {}
