package com.examen.badwallet_api.payment.factory;

import org.springframework.stereotype.Component;

import com.examen.badwallet_api.payment.enums.ServiceName;

@Component
public class FactureServiceFactory {
    
        public ServiceName resolve(String serviceName) {
        try {
            return ServiceName.valueOf(serviceName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Service de facture inconnu: " + serviceName);
        }
    }
}
