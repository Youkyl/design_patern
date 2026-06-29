package com.examen.badwallet_api.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DepositStrategyFactory {

    private final List<DepositStrategy> strategies;
    private Map<String, DepositStrategy> strategyMap;

    public DepositStrategy get(String methodName) {
        if (strategyMap == null) {
            strategyMap = strategies.stream()
                    .collect(Collectors.toMap(DepositStrategy::getMethodName, s -> s));
        }
        DepositStrategy strategy = strategyMap.get(methodName);
        if (strategy == null) {
            throw new IllegalArgumentException("Méthode de paiement inconnue: " + methodName);
        }
        return strategy;
    }
}
