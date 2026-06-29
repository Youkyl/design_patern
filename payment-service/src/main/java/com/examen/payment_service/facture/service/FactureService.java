package com.examen.payment_service.facture.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.examen.payment_service.facture.dto.FactureResponse;
import com.examen.payment_service.facture.dto.PayFacturesRequest;
import com.examen.payment_service.facture.dto.PayRequest;
import com.examen.payment_service.facture.dto.PayResponse;
import com.examen.payment_service.facture.enums.ServiceName;
import com.examen.payment_service.facture.models.Facture;
import com.examen.payment_service.facture.repository.FactureRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FactureService {

    private final FactureRepository factureRepository;

    public List<FactureResponse> getCurrent(String walletCode, String unite) {
        List<Facture> factures = (unite == null || unite.isBlank())
                ? factureRepository.findByWalletCodeAndPaidFalse(walletCode)
                : factureRepository.findByWalletCodeAndServiceNameAndPaidFalse(walletCode, ServiceName.valueOf(unite));

        return factures.stream().map(this::toResponse).toList();
    }

    public List<FactureResponse> getPeriode(String walletCode, LocalDate debut, LocalDate fin) {
        return factureRepository.findByWalletCodeAndPaidFalseAndDueDateBetween(walletCode, debut, fin)
                .stream().map(this::toResponse).toList();
    }

    public PayResponse pay(PayRequest request) {
        ServiceName serviceName = ServiceName.valueOf(request.serviceName());
        List<Facture> factures = factureRepository.findByWalletCodeAndServiceNameAndPaidFalse(request.walletCode(), serviceName);

        if (factures.isEmpty()) {
            throw new NoSuchElementException("Aucune facture impayée pour " + request.walletCode() + " / " + serviceName);
        }

        factures.forEach(f -> f.setPaid(true));
        factureRepository.saveAll(factures);

        BigDecimal total = factures.stream().map(Facture::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        List<String> references = factures.stream().map(Facture::getReference).toList();

        return new PayResponse("Paiement effectué avec succès", total, references);
    }

    public PayResponse payFactures(PayFacturesRequest request) {
        List<Facture> factures = factureRepository.findByReferenceIn(request.factureReferences());

        if (factures.isEmpty()) {
            throw new NoSuchElementException("Aucune facture trouvée pour les références fournies");
        }

        factures.forEach(f -> f.setPaid(true));
        factureRepository.saveAll(factures);

        BigDecimal total = factures.stream().map(Facture::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        List<String> references = factures.stream().map(Facture::getReference).toList();

        return new PayResponse("Factures payées avec succès", total, references);
    }

    private FactureResponse toResponse(Facture f) {
        return new FactureResponse(f.getReference(), f.getWalletCode(), f.getServiceName().name(),
                f.getAmount(), f.getDueDate(), f.isPaid());
    }
}
