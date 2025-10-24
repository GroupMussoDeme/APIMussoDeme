package com.mussodeme.MussoDeme.controllers;

import com.mussodeme.MussoDeme.dto.HistoriqueDTO;
import com.mussodeme.MussoDeme.dto.Response;
import com.mussodeme.MussoDeme.services.HistoriqueService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Controller REST pour gérer les historiques
 * Permet de consulter l'historique des actions : ventes, achats, téléchargements, paiements
 */
@RestController
@RequestMapping("/api/historiques")
public class HistoriqueController {

    private static final Logger logger = Logger.getLogger(HistoriqueController.class.getName());

    private final HistoriqueService historiqueService;

    // Constructor for dependency injection
    public HistoriqueController(HistoriqueService historiqueService) {
        this.historiqueService = historiqueService;
    }

    //================== ENDPOINTS UTILISATEURS ==================

    /**
     * Récupérer tous mes historiques
     * GET /api/historiques/utilisateur/{utilisateurId}
     */
    @GetMapping("/utilisateur/{utilisateurId}")
    @PreAuthorize("hasAnyRole('FEMME_RURALE', 'ADMIN')")
    public ResponseEntity<Response> getMesHistoriques(@PathVariable Long utilisateurId) {
        logger.info("[API] Récupération des historiques de l'utilisateur " + utilisateurId);
        
        List<HistoriqueDTO> historiques = historiqueService.getHistoriquesUtilisateur(utilisateurId);
        
        return ResponseEntity.ok(
            Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message(historiques.size() + " historique(s) trouvé(s)")
                .data(historiques)
                .build()
        );
    }

    /**
     * Récupérer mes historiques par type
     * GET /api/historiques/utilisateur/{utilisateurId}/type/{typeHistorique}
     */
    @GetMapping("/utilisateur/{utilisateurId}/type/{typeHistorique}")
    @PreAuthorize("hasAnyRole('FEMME_RURALE', 'ADMIN')")
    public ResponseEntity<Response> getMesHistoriquesParType(
            @PathVariable Long utilisateurId,
            @PathVariable String typeHistorique) {
        
        logger.info("[API] Historiques de type " + typeHistorique + " pour l'utilisateur " + utilisateurId);
        
        List<HistoriqueDTO> historiques = historiqueService.getHistoriquesParType(
            utilisateurId, typeHistorique
        );
        
        return ResponseEntity.ok(
            Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message(historiques.size() + " historique(s) de type " + typeHistorique)
                .data(historiques)
                .build()
        );
    }

    /**
     * Récupérer mes historiques sur une période
     * GET /api/historiques/utilisateur/{utilisateurId}/periode
     * ?dateDebut=2025-01-01T00:00:00&dateFin=2025-12-31T23:59:59
     */
    @GetMapping("/utilisateur/{utilisateurId}/periode")
    @PreAuthorize("hasAnyRole('FEMME_RURALE', 'ADMIN')")
    public ResponseEntity<Response> getMesHistoriquesParPeriode(
            @PathVariable Long utilisateurId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin) {
        
        logger.info("[API] Historiques de l'utilisateur " + utilisateurId + " entre " + dateDebut + " et " + dateFin);
        
        List<HistoriqueDTO> historiques = historiqueService.getHistoriquesParPeriode(
            utilisateurId, dateDebut, dateFin
        );
        
        return ResponseEntity.ok(
            Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message(historiques.size() + " historique(s) trouvé(s) sur la période")
                .data(historiques)
                .build()
        );
    }

    /**
     * Obtenir des statistiques par type
     * GET /api/historiques/utilisateur/{utilisateurId}/stats/{typeHistorique}
     */
    @GetMapping("/utilisateur/{utilisateurId}/stats/{typeHistorique}")
    @PreAuthorize("hasAnyRole('FEMME_RURALE', 'ADMIN')")
    public ResponseEntity<Response> getStatistiques(
            @PathVariable Long utilisateurId,
            @PathVariable String typeHistorique) {
        
        logger.info("[API] Statistiques " + typeHistorique + " pour l'utilisateur " + utilisateurId);
        
        Long count = historiqueService.getStatistiquesParType(utilisateurId, typeHistorique);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("utilisateurId", utilisateurId);
        stats.put("typeHistorique", typeHistorique);
        stats.put("total", count);
        
        return ResponseEntity.ok(
            Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Statistiques récupérées")
                .data(stats)
                .build()
        );
    }

    //================== ENDPOINTS ADMIN ==================

    /**
     * Récupérer tous les historiques (ADMIN)
     * GET /api/historiques/tous
     */
    @GetMapping("/tous")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> getTousLesHistoriques() {
        logger.info("[API] Récupération de tous les historiques (ADMIN)");
        
        List<HistoriqueDTO> historiques = historiqueService.getTousLesHistoriques();
        
        return ResponseEntity.ok(
            Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message(historiques.size() + " historique(s) trouvé(s)")
                .data(historiques)
                .build()
        );
    }

    /**
     * Récupérer les historiques globaux par type (ADMIN)
     * GET /api/historiques/tous/type/{typeHistorique}
     */
    @GetMapping("/tous/type/{typeHistorique}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> getHistoriquesGlobauxParType(@PathVariable String typeHistorique) {
        logger.info("[API] Historiques globaux de type " + typeHistorique + " (ADMIN)");
        
        List<HistoriqueDTO> historiques = historiqueService.getHistoriquesGlobauxParType(typeHistorique);
        
        return ResponseEntity.ok(
            Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message(historiques.size() + " historique(s) de type " + typeHistorique)
                .data(historiques)
                .build()
        );
    }

    //================== ENDPOINTS DE RÉFÉRENCE ==================

    /**
     * Lister les types d'historiques disponibles
     * GET /api/historiques/types
     */
    @GetMapping("/types")
    public ResponseEntity<Response> getTypesHistoriques() {
        logger.info("[API] Liste des types d'historiques");
        
        Map<String, String> types = new HashMap<>();
        types.put("HISTORIQUE_DES_VENTES", "Historique des ventes de produits");
        types.put("HISTORIQUE_DES_ACHATS", "Historique des achats/commandes");
        types.put("HISTORIQUE_DES_TELECHARGEMENTS", "Historique des téléchargements de contenus");
        types.put("HISTORIQUE_DES_PAIEMENTS", "Historique des paiements");
        types.put("HISTORIQUE_DES_PARTAGES", "Historique des partages dans les coopératives");
        types.put("HISTORIQUE_DES_PUBLICATIONS", "Historique des publications/modifications de produits");
        types.put("HISTORIQUE_ADHESIONS_COOPERATIVES", "Historique des adhésions aux coopératives");
        
        return ResponseEntity.ok(
            Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Types d'historiques disponibles")
                .data(types)
                .build()
        );
    }
}