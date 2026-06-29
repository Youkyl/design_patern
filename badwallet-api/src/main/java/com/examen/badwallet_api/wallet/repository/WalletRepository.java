package com.examen.badwallet_api.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.examen.badwallet_api.wallet.model.Wallet;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByPhone(String phone);
    boolean existsByPhone(String phone);
}
