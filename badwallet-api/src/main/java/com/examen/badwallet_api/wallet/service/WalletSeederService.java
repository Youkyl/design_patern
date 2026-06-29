package com.examen.badwallet_api.wallet.service;

import com.examen.badwallet_api.transaction.enums.TransactionType;
import com.examen.badwallet_api.transaction.models.Transaction;
import com.examen.badwallet_api.transaction.repository.TransactionRepository;
import com.examen.badwallet_api.wallet.model.Wallet;
import com.examen.badwallet_api.wallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class WalletSeederService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final Random random = new Random();

    @Async
    public void seed(int numWallets, int eventsPerWallet) {
        for (int i = 1; i <= numWallets; i++) {
            Wallet wallet = Wallet.builder()
                    .phone("+22177" + String.format("%07d", i))
                    .email("seed" + i + "@test.com")
                    .balance(new BigDecimal("100000"))
                    .code(String.format("WLT-SEED%04d", i))
                    .currency("XOF")
                    .build();

            Wallet saved = walletRepository.save(wallet);

            for (int j = 0; j < eventsPerWallet; j++) {
                BigDecimal amount = new BigDecimal(100 + random.nextInt(5000));
                transactionRepository.save(Transaction.builder()
                        .wallet(saved)
                        .type(TransactionType.DEPOSIT)
                        .amount(amount)
                        .paymentMethod("CREDIT_CARD")
                        .createdAt(LocalDateTime.now())
                        .build());
            }
        }
    }
}