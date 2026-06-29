package com.examen.badwallet_api.payment.client;

import com.examen.badwallet_api.payment.dto.FactureDto;
import com.examen.badwallet_api.payment.dto.PaymentConfirmation;

import java.time.LocalDate;
import java.util.List;

public interface FactureApiClient {
    List<FactureDto> getCurrent(String walletCode, String unite);
    List<FactureDto> getPeriode(String walletCode, LocalDate debut, LocalDate fin);
    PaymentConfirmation pay(String walletCode, String serviceName, java.math.BigDecimal amount);
    PaymentConfirmation payFactures(String walletCode, String serviceName, List<String> references);
}
