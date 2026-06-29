package com.examen.payment_service.facture.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.examen.payment_service.facture.enums.ServiceName;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "factures")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @jakarta.persistence.Column(unique = true, nullable = false)
    private String reference;

    private String walletCode;

    @Enumerated(EnumType.STRING)
    private ServiceName serviceName;

    private BigDecimal amount;

    private LocalDate dueDate;

    private boolean paid;
}
