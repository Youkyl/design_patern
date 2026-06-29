package com.examen.badwallet_api.payment.dto;

import java.util.List;

public record ExternalPayFacturesRequest(
    String walletCode,
    String serviceName,
    List<String> factureReferences
) {}
