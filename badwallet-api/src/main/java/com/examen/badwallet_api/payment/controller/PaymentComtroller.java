package com.examen.badwallet_api.payment.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.examen.badwallet_api.payment.dto.PayFacturesRequest;
import com.examen.badwallet_api.payment.dto.PayRequest;
import com.examen.badwallet_api.payment.service.PaymentService;
import com.examen.badwallet_api.wallet.dto.WalletResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/wallets/payments")
@RequiredArgsConstructor
public class PaymentComtroller {

    private final PaymentService paymentServ;

    @PostMapping("/pay")
    public WalletResponse pay(@Valid @RequestBody PayRequest request) {
        return paymentServ.pay(request);
    }

    @PostMapping("/pay-factures")
    public WalletResponse payFactures(@Valid @RequestBody PayFacturesRequest request) {
        return paymentServ.payFactures(request);
    }
        
}
