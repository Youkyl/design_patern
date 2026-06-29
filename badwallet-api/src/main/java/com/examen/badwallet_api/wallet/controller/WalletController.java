package com.examen.badwallet_api.wallet.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.examen.badwallet_api.wallet.dto.CreateWalletRequest;
import com.examen.badwallet_api.wallet.dto.WalletResponse;
import com.examen.badwallet_api.wallet.service.WalletService;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WalletResponse createWallet(@Valid @RequestBody CreateWalletRequest request) {
        return walletService.createWallet(request);
    }

    @GetMapping
    public Page<WalletResponse> listWallets(Pageable pageable) {
        return walletService.listWallets(pageable);
    }

    @GetMapping("/{phone}")
    public WalletResponse getWallet(@PathVariable String phone) {
        return walletService.getByphone(phone);
    }

    @GetMapping("/{phone}/balance")
    public BigDecimal getBalance(@PathVariable String phone) {
        return walletService.getBalance(phone);
    }

}