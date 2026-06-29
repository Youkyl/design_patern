package com.examen.payment_service.facture.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.examen.payment_service.facture.enums.ServiceName;
import com.examen.payment_service.facture.models.Facture;

public interface FactureRepository extends JpaRepository<Facture, Long> {

    List<Facture> findByWalletCodeAndPaidFalse(String walletCode);

    List<Facture> findByWalletCodeAndServiceNameAndPaidFalse(String walletCode, ServiceName serviceName);

    List<Facture> findByWalletCodeAndPaidFalseAndDueDateBetween(String walletCode, LocalDate debut, LocalDate fin);

    List<Facture> findByReferenceIn(List<String> references);
}
