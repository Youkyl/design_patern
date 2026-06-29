package com.examen.badwallet_api.payment.service;


import com.examen.badwallet_api.payment.client.FactureApiClient;
import com.examen.badwallet_api.payment.dto.PayFacturesRequest;
import com.examen.badwallet_api.payment.dto.PayRequest;
import com.examen.badwallet_api.payment.dto.PaymentConfirmation;
import com.examen.badwallet_api.payment.factory.FactureServiceFactory;
import com.examen.badwallet_api.transaction.enums.TransactionType;
import com.examen.badwallet_api.transaction.models.Transaction;
import com.examen.badwallet_api.transaction.repository.TransactionRepository;
import com.examen.badwallet_api.wallet.dto.WalletResponse;
import com.examen.badwallet_api.wallet.model.Wallet;
import com.examen.badwallet_api.wallet.repository.WalletRepository;
import com.examen.badwallet_api.wallet.service.WalletService;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    
    private final WalletRepository walletRepository;
    private final WalletService walletServ;
    private final TransactionRepository transactionRepository;
    private final FactureApiClient factureApiClient;
    private final FactureServiceFactory factureServiceFactory;

    public WalletResponse pay(PayRequest request) {
        Wallet wallet = walletRepository.findByPhone(request.phone())
                .orElseThrow(() -> new NoSuchElementException("Wallet introuvable pour " + request.phone()));

        factureServiceFactory.resolve(request.serviceName());

        PaymentConfirmation confirmation = factureApiClient.pay(wallet.getCode(), request.serviceName(), request.amount());

        return debiterEtJournaliser(wallet, confirmation);
    }

    public WalletResponse payFactures(PayFacturesRequest request) {
        Wallet wallet = walletRepository.findByPhone(request.phone())
                .orElseThrow(() -> new NoSuchElementException("Wallet introuvable pour " + request.phone()));

        factureServiceFactory.resolve(request.serviceName());

        PaymentConfirmation confirmation = factureApiClient.payFactures(wallet.getCode(), request.serviceName(), request.factureReferences());

        return debiterEtJournaliser(wallet, confirmation);
    }

    private WalletResponse debiterEtJournaliser(Wallet wallet, PaymentConfirmation confirmation) {
        if (wallet.getBalance().compareTo(confirmation.totalPaid()) < 0) {
            throw new IllegalStateException("Solde insuffisant pour ce paiement");
        }

        wallet.setBalance(wallet.getBalance().subtract(confirmation.totalPaid()));
        Wallet saved = walletRepository.save(wallet);

        transactionRepository.save(Transaction.builder()
                .wallet(saved)
                .type(TransactionType.PAYMENT)
                .amount(confirmation.totalPaid())
                .createdAt(LocalDateTime.now())
                .build());

        return walletServ.toResponse(saved);
    }
}
