package com.examen.badwallet_api.transaction.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.examen.badwallet_api.strategy.DepositStrategy;
import com.examen.badwallet_api.strategy.DepositStrategyFactory;
import com.examen.badwallet_api.transaction.dto.DepositRequest;
import com.examen.badwallet_api.transaction.dto.TransactionResponse;
import com.examen.badwallet_api.transaction.dto.TransferRequest;
import com.examen.badwallet_api.transaction.dto.WithdrawRequest;
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

    private static final BigDecimal RATE = new BigDecimal("0.01");
    private static final BigDecimal CAP = new BigDecimal("5000");


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

    public BigDecimal computeFee(BigDecimal amount) {
        BigDecimal fee = amount.multiply(RATE).setScale(0, RoundingMode.HALF_UP);
        return fee.min(CAP);
    }

    public WalletResponse withdraw(WithdrawRequest request) {
        Wallet wallet = walletRepository.findByPhone(request.phone())
                .orElseThrow(() -> new NoSuchElementException("Wallet introuvable pour " + request.phone()));

        BigDecimal fee = this.computeFee(request.amount());
        BigDecimal totalDebit = request.amount().add(fee);

        if (wallet.getBalance().compareTo(totalDebit) < 0) {
            throw new IllegalStateException("Solde insuffisant");
        }

        wallet.setBalance(wallet.getBalance().subtract(totalDebit));
        Wallet saved = walletRepository.save(wallet);

        transactionRepository.save(Transaction.builder()
                .wallet(saved)
                .type(TransactionType.WITHDRAW)
                .amount(request.amount())
                .createdAt(LocalDateTime.now())
                .build());

        return walletServ.toResponse(saved);
    }

    public WalletResponse transfer(TransferRequest request) {
        Wallet sender = walletRepository.findByPhone(request.senderPhone())
                .orElseThrow(() -> new NoSuchElementException("Wallet introuvable pour " + request.senderPhone()));
        Wallet receiver = walletRepository.findByPhone(request.receiverPhone())
                .orElseThrow(() -> new NoSuchElementException("Wallet introuvable pour " + request.receiverPhone()));

        if (sender.getBalance().compareTo(request.amount()) < 0) {
            throw new IllegalStateException("Solde insuffisant");
        }

        sender.setBalance(sender.getBalance().subtract(request.amount()));
        receiver.setBalance(receiver.getBalance().add(request.amount()));

        Wallet savedSender = walletRepository.save(sender);
        walletRepository.save(receiver);

        transactionRepository.save(Transaction.builder()
                .wallet(savedSender)
                .type(TransactionType.TRANSFER)
                .amount(request.amount())
                .createdAt(LocalDateTime.now())
                .build());

        return walletServ.toResponse(savedSender);
    }

    public List<TransactionResponse> getHistory(String phone) {
        return transactionRepository.findByWallet_Phone(phone)
                .stream()
                .map(t -> new TransactionResponse(t.getId(), t.getType(), t.getAmount(), t.getPaymentMethod(), t.getCreatedAt()))
                .toList();
    }
}
