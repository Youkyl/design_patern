package com.examen.badwallet_api.payment.dto;

import java.math.BigDecimal;

public record ExternalPayRequest(
    String walletCode,
    String serviceName,
    BigDecimal amount
) {}
