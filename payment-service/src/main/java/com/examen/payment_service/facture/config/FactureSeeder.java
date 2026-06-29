package com.examen.payment_service.facture.config;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.examen.payment_service.facture.enums.ServiceName;
import com.examen.payment_service.facture.models.Facture;
import com.examen.payment_service.facture.repository.FactureRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FactureSeeder implements CommandLineRunner {

    private final FactureRepository factureRepository;

    @Override
    public void run(String... args) {
        if (factureRepository.count() > 0) {
            return;
        }

        LocalDate dueDate = LocalDate.now().withDayOfMonth(28);

        for (int walletNum = 1; walletNum <= 10; walletNum++) {
            String walletCode = String.format("WLT-%07d", walletNum);

            for (int i = 1; i <= 3; i++) {
                factureRepository.save(Facture.builder()
                        .reference("FAC-ISM-" + walletNum + "-" + i)
                        .walletCode(walletCode)
                        .serviceName(ServiceName.ISM)
                        .amount(new BigDecimal("2500"))
                        .dueDate(dueDate)
                        .paid(false)
                        .build());
            }

            for (int i = 1; i <= 2; i++) {
                factureRepository.save(Facture.builder()
                        .reference("FAC-WOYAFAL-" + walletNum + "-" + i)
                        .walletCode(walletCode)
                        .serviceName(ServiceName.WOYAFAL)
                        .amount(new BigDecimal("4000"))
                        .dueDate(dueDate)
                        .paid(false)
                        .build());
            }
        }
    }
}
