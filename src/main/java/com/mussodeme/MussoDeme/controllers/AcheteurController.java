package com.mussodeme.MussoDeme.controllers;

import com.mussodeme.MussoDeme.services.AcheteurService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/acheteurs")
public class AcheteurController {

    private final AcheteurService acheteurService;

    public AcheteurController(AcheteurService acheteurService) {
        this.acheteurService = acheteurService;
    }

    // Voir les produits disponibles
    @GetMapping("/produits")
    public List<Map<String, Object>> voirProduits() {
        return acheteurService.voirProduits();
    }

    // Démarrer un chat vocal avec une vendeuse
    @PostMapping("/{acheteurId}/chat-vocal")
    public String demarrerChat(
            @PathVariable Long acheteurId,
            @RequestParam Long vendeuseId) {
        return acheteurService.demarrerChatVocal(acheteurId, vendeuseId);
    }

    // Payer une commande par Mobile Money
    @PostMapping("/{acheteurId}/payer")
    public String payerCommande(
            @PathVariable Long acheteurId,
            @RequestParam Long commandeId) {
        return acheteurService.payerCommande(acheteurId, commandeId);
    }
}
