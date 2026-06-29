package com.examen.badwallet_api.strategy;

import com.examen.badwallet_api.wallet.model.Wallet;

import java.math.BigDecimal;

public interface DepositStrategy {
    void apply(Wallet wallet, BigDecimal amount);
    String getMethodName();
}
