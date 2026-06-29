package com.examen.badwallet_api.transaction.service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.examen.badwallet_api.strategy.DepositStrategy;
import com.examen.badwallet_api.strategy.DepositStrategyFactory;
import com.examen.badwallet_api.transaction.dto.DepositRequest;
import com.examen.badwallet_api.transaction.enums.TransactionType;
import com.examen.badwallet_api.transaction.models.Transaction;
import com.examen.badwallet_api.transaction.repository.TransactionRepository;
import com.examen.badwallet_api.wallet.dto.WalletResponse;
import com.examen.badwallet_api.wallet.model.Wallet;
import com.examen.badwallet_api.wallet.repository.WalletRepository;
import com.examen.badwallet_api.wallet.service.WalletService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class TransactionServce {

    private final WalletRepository walletRepository;
    private final WalletService walletServ;
    private final TransactionRepository transactionRepository;
    private final DepositStrategyFactory depositStrategyFactory;



    public WalletResponse deposit(Long walletId, DepositRequest request) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new NoSuchElementException("Wallet introuvable: " + walletId));

        DepositStrategy strategy = depositStrategyFactory.get(request.paymentMethod());
        strategy.apply(wallet, request.amount());
        Wallet saved = walletRepository.save(wallet);

        transactionRepository.save(Transaction.builder()
                .wallet(saved)
                .type(TransactionType.DEPOSIT)
                .amount(request.amount())
                .paymentMethod(request.paymentMethod())
                .createdAt(LocalDateTime.now())
                .build());

        return walletServ.toResponse(saved);
    }
}
