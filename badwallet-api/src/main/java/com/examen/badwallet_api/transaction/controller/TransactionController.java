package com.examen.badwallet_api.transaction.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.examen.badwallet_api.transaction.dto.DepositRequest;
import com.examen.badwallet_api.transaction.dto.TransferRequest;
import com.examen.badwallet_api.transaction.dto.WithdrawRequest;
import com.examen.badwallet_api.transaction.service.TransactionServce;
import com.examen.badwallet_api.wallet.dto.WalletResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/wallets/transactions")
@RequiredArgsConstructor

public class TransactionController {

    
    private final TransactionServce transacServ;

    
    @PostMapping("/{id}/deposit")
    public WalletResponse deposit(@PathVariable Long id, @Valid @RequestBody DepositRequest request) {
        return transacServ.deposit(id, request);
    }

    @PostMapping("/withdraw")
    public WalletResponse withdraw(@Valid @RequestBody WithdrawRequest request) {
        return transacServ.withdraw(request);
    }

    @PostMapping("/transfer")
    public WalletResponse transfer(@Valid @RequestBody TransferRequest request) {
        return transacServ.transfer(request);
    }
        
}
