package com.examen.badwallet_api.wallet.service;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.examen.badwallet_api.wallet.dto.CreateWalletRequest;
import com.examen.badwallet_api.wallet.dto.WalletResponse;
import com.examen.badwallet_api.wallet.model.Wallet;
import com.examen.badwallet_api.wallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WalletService {
    
    private final WalletRepository walletRepository;

    public WalletResponse createWallet(CreateWalletRequest request) {
        if (walletRepository.existsByPhone(request.phone())) {
            throw new IllegalArgumentException("Un portefeuille existe déjà pour ce numéro");
        }

        Wallet wallet = Wallet.builder()
                .phone(request.phone())
                .email(request.email())
                .balance(request.initialBalance())
                .code(request.code())
                .currency(request.currency())
                .build();

        Wallet saved = walletRepository.save(wallet);

        return new WalletResponse(
                saved.getId(), saved.getPhone(), saved.getEmail(),
                saved.getBalance(), saved.getCode(), saved.getCurrency()
        );
    }

    public Page<WalletResponse> listWallets(Pageable pageable) {
        return walletRepository.findAll(pageable).map(this::toResponse);
    }

    public WalletResponse getByphone(String phone) {
        Wallet wallet = walletRepository.findByPhone(phone)
                .orElseThrow(() -> new NoSuchElementException("Wallet introuvable pour " + phone));
        return toResponse(wallet);
    }

    public BigDecimal getBalance(String phone) {
        return walletRepository.findByPhone(phone)
                .map(Wallet::getBalance)
                .orElseThrow(() -> new NoSuchElementException("Wallet introuvable pour " + phone));
    }

    private WalletResponse toResponse(Wallet w) {
        return new WalletResponse(w.getId(), w.getPhone(), w.getEmail(),
                w.getBalance(), w.getCode(), w.getCurrency());
    }

}
