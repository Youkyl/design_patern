package com.examen.badwallet_api.wallet.dto;

import java.math.BigDecimal;

public record WalletResponse(
    Long id,
    String phone,
    String email,
    BigDecimal balance,
    String code,
    String currency
) {}
