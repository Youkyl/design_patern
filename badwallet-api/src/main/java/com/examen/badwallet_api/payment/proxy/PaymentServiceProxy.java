package com.examen.badwallet_api.payment.proxy;

import com.examen.badwallet_api.payment.client.FactureApiClient;
import com.examen.badwallet_api.payment.dto.ExternalPayFacturesRequest;
import com.examen.badwallet_api.payment.dto.ExternalPayRequest;
import com.examen.badwallet_api.payment.dto.FactureDto;
import com.examen.badwallet_api.payment.dto.PaymentConfirmation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class PaymentServiceProxy implements FactureApiClient{

    private final RestClient restClient;

    public PaymentServiceProxy(@Value("${payment-service.base-url}") String baseUrl) {
        this.restClient = RestClient.create(baseUrl);
    }

    @Override
    public List<FactureDto> getCurrent(String walletCode, String unite) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/factures/{walletCode}/current")
                        .queryParamIfPresent("unite", java.util.Optional.ofNullable(unite))
                        .build(walletCode))
                .retrieve()
                .body(new org.springframework.core.ParameterizedTypeReference<List<FactureDto>>() {});
    }

    @Override
    public List<FactureDto> getPeriode(String walletCode, LocalDate debut, LocalDate fin) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/factures/{walletCode}/periode")
                        .queryParam("debut", debut)
                        .queryParam("fin", fin)
                        .build(walletCode))
                .retrieve()
                .body(new org.springframework.core.ParameterizedTypeReference<List<FactureDto>>() {});
    }

    @Override
    public PaymentConfirmation pay(String walletCode, String serviceName, BigDecimal amount) {
        return restClient.post()
                .uri("/api/factures/pay")
                .body(new ExternalPayRequest(walletCode, serviceName, amount))
                .retrieve()
                .body(PaymentConfirmation.class);
    }

    @Override
    public PaymentConfirmation payFactures(String walletCode, String serviceName, List<String> references) {
        return restClient.post()
                .uri("/api/factures/pay-factures")
                .body(new ExternalPayFacturesRequest(walletCode, serviceName, references))
                .retrieve()
                .body(PaymentConfirmation.class);
    }   
}
