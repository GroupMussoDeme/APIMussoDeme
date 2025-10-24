package com.mussodeme.MussoDeme.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncConfig {
    // Active l'exécution asynchrone pour les méthodes @Async
    // Permet l'envoi d'emails en arrière-plan sans bloquer les requêtes
}
