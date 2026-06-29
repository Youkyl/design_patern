package com.examen.badwallet_api.payment.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.examen.badwallet_api.payment.client.FactureApiClient;
import com.examen.badwallet_api.payment.dto.FactureDto;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/external/factures")
@RequiredArgsConstructor
public class actureProxyController {

    private final FactureApiClient factureApiClient;
    @GetMapping("/{walletCode}/current")

    public List<FactureDto> getCurrent(@PathVariable String walletCode,
                                        @RequestParam(required = false) String unite) {
        return factureApiClient.getCurrent(walletCode, unite);
    }

    @GetMapping("/{walletCode}/periode")
    public List<FactureDto> getPeriode(@PathVariable String walletCode,
                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return factureApiClient.getPeriode(walletCode, debut, fin);
    }
}
