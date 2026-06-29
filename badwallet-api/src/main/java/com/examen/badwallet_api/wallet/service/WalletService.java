package com.examen.badwallet_api.wallet.service;

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

}
