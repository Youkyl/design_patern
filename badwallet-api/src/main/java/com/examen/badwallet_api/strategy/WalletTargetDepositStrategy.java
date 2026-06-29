package com.examen.badwallet_api.strategy;

import com.examen.badwallet_api.wallet.model.Wallet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class WalletTargetDepositStrategy implements DepositStrategy {

    @Override
    public void apply(Wallet wallet, BigDecimal amount) {
        wallet.setBalance(wallet.getBalance().add(amount));
    }

    @Override
    public String getMethodName() {
        return "WALLET_TARGET";
    }
}
