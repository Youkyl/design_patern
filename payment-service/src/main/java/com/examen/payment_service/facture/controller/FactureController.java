package com.examen.payment_service.facture.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.examen.payment_service.facture.dto.FactureResponse;
import com.examen.payment_service.facture.dto.PayFacturesRequest;
import com.examen.payment_service.facture.dto.PayRequest;
import com.examen.payment_service.facture.dto.PayResponse;
import com.examen.payment_service.facture.service.FactureService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/factures")
@RequiredArgsConstructor
public class FactureController {

    private final FactureService factureService;

    @GetMapping("/{walletCode}/current")
    public List<FactureResponse> getCurrent(@PathVariable String walletCode,
                                             @RequestParam(required = false) String unite) {
        return factureService.getCurrent(walletCode, unite);
    }

    @GetMapping("/{walletCode}/periode")
    public List<FactureResponse> getPeriode(@PathVariable String walletCode,
                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return factureService.getPeriode(walletCode, debut, fin);
    }

    @PostMapping("/pay")
    public PayResponse pay(@Valid @RequestBody PayRequest request) {
        return factureService.pay(request);
    }

    @PostMapping("/pay-factures")
    public PayResponse payFactures(@Valid @RequestBody PayFacturesRequest request) {
        return factureService.payFactures(request);
    }
}
