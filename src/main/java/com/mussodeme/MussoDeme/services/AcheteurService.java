package com.mussodeme.MussoDeme.services;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AcheteurService {

    // panier / liste de produits visibles
    private final List<Map<String, Object>> produitsDisponibles = new ArrayList<>();

    // Voir les produits publiés (avec audio de guidage)
    public List<Map<String, Object>> voirProduits() {
        // Exemple de données simulées
        if (produitsDisponibles.isEmpty()) {
            produitsDisponibles.add(Map.of(
                    "nom", "Beurre de karité",
                    "description", "Beurre pur localement produit",
                    "audioGuidage", "audio/karite.mp3"
            ));
            produitsDisponibles.add(Map.of(
                    "nom", "Savon noir",
                    "description", "Savon traditionnel naturel",
                    "audioGuidage", "audio/savon.mp3"
            ));
        }
        return produitsDisponibles;
    }

    // Faire un chat vocal avec la vendeuse
    public String demarrerChatVocal(Long acheteurId, Long vendeuseId) {
        // Simulation d’un appel vocal ou WebSocket
        return "Chat vocal démarré entre l’acheteur " + acheteurId + " et la vendeuse " + vendeuseId;
    }

    // Payer par mobile money
    public String payerCommande(Long acheteurId, Long commandeId) {
        // Ici, tu pourrais intégrer un service de paiement (Orange Money, Moov Money, etc.)
        return "Paiement de la commande " + commandeId + " effectué avec succès via Mobile Money par l’acheteur " + acheteurId;
    }
}
