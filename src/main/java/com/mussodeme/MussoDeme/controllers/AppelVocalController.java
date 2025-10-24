package com.mussodeme.MussoDeme.controllers;

import com.mussodeme.MussoDeme.dto.AppelVocalDTO;
import com.mussodeme.MussoDeme.dto.Response;
import com.mussodeme.MussoDeme.services.AppelVocalService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appels")
public class AppelVocalController {
    
    private final AppelVocalService appelVocalService;
    
    // Constructor for dependency injection
    public AppelVocalController(AppelVocalService appelVocalService) {
        this.appelVocalService = appelVocalService;
    }
    
    /**
     * Initier un appel vocal privé
     */
    @PostMapping("/prive/{appeleId}")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> initierAppelPrive(
            @RequestHeader("userId") Long appelantId,
            @PathVariable Long appeleId,
            @RequestParam String audioUrl) {
        try {
            AppelVocalDTO appel = appelVocalService.initierAppelPrive(appelantId, appeleId, audioUrl);
            
            return ResponseEntity.ok(Response.builder()
                    .responseCode("200")
                    .responseMessage("Appel vocal privé initié avec succès")
                    .data(appel)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.builder()
                    .responseCode("400")
                    .responseMessage("Erreur : " + e.getMessage())
                    .build());
        }
    }
    
    /**
     * Initier un appel vocal de groupe dans une coopérative
     */
    @PostMapping("/groupe/{cooperativeId}")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> initierAppelGroupe(
            @RequestHeader("userId") Long appelantId,
            @PathVariable Long cooperativeId,
            @RequestParam String audioUrl) {
        try {
            AppelVocalDTO appel = appelVocalService.initierAppelGroupe(appelantId, cooperativeId, audioUrl);
            
            return ResponseEntity.ok(Response.builder()
                    .responseCode("200")
                    .responseMessage("Appel vocal de groupe initié avec succès")
                    .data(appel)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.builder()
                    .responseCode("400")
                    .responseMessage("Erreur : " + e.getMessage())
                    .build());
        }
    }
    
    /**
     * Répondre à un appel vocal
     */
    @PutMapping("/{appelId}/repondre")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> repondreAppel(
            @RequestHeader("userId") Long appeleId,
            @PathVariable Long appelId,
            @RequestParam String reponseAudioUrl) {
        try {
            AppelVocalDTO appel = appelVocalService.repondreAppel(appelId, appeleId, reponseAudioUrl);
            
            return ResponseEntity.ok(Response.builder()
                    .responseCode("200")
                    .responseMessage("Appel vocal répondu avec succès")
                    .data(appel)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.builder()
                    .responseCode("400")
                    .responseMessage("Erreur : " + e.getMessage())
                    .build());
        }
    }
    
    /**
     * Refuser un appel vocal
     */
    @PutMapping("/{appelId}/refuser")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> refuserAppel(
            @RequestHeader("userId") Long appeleId,
            @PathVariable Long appelId) {
        try {
            appelVocalService.refuserAppel(appelId, appeleId);
            
            return ResponseEntity.ok(Response.builder()
                    .responseCode("200")
                    .responseMessage("Appel vocal refusé avec succès")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.builder()
                    .responseCode("400")
                    .responseMessage("Erreur : " + e.getMessage())
                    .build());
        }
    }
    
    /**
     * Laisser un message vocal (messagerie vocale)
     */
    @PostMapping("/{appeleId}/message-vocal")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> laisserMessageVocal(
            @RequestHeader("userId") Long appelantId,
            @PathVariable Long appeleId,
            @RequestParam String messageAudioUrl) {
        try {
            AppelVocalDTO message = appelVocalService.laisserMessageVocal(appelantId, appeleId, messageAudioUrl);
            
            return ResponseEntity.ok(Response.builder()
                    .responseCode("200")
                    .responseMessage("Message vocal laissé avec succès")
                    .data(message)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.builder()
                    .responseCode("400")
                    .responseMessage("Erreur : " + e.getMessage())
                    .build());
        }
    }
    
    /**
     * Récupérer les appels reçus
     */
    @GetMapping("/recus")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> getAppelsRecus(@RequestHeader("userId") Long femmeId) {
        try {
            List<AppelVocalDTO> appels = appelVocalService.getAppelsRecus(femmeId);
            
            return ResponseEntity.ok(Response.builder()
                    .responseCode("200")
                    .responseMessage("Appels reçus récupérés avec succès")
                    .data(appels)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.builder()
                    .responseCode("400")
                    .responseMessage("Erreur : " + e.getMessage())
                    .build());
        }
    }
    
    /**
     * Récupérer les appels émis
     */
    @GetMapping("/emis")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> getAppelsEmis(@RequestHeader("userId") Long femmeId) {
        try {
            List<AppelVocalDTO> appels = appelVocalService.getAppelsEmis(femmeId);
            
            return ResponseEntity.ok(Response.builder()
                    .responseCode("200")
                    .responseMessage("Appels émis récupérés avec succès")
                    .data(appels)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.builder()
                    .responseCode("400")
                    .responseMessage("Erreur : " + e.getMessage())
                    .build());
        }
    }
    
    /**
     * Récupérer les appels d'une coopérative
     */
    @GetMapping("/cooperative/{cooperativeId}")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> getAppelsCooperative(@PathVariable Long cooperativeId) {
        try {
            List<AppelVocalDTO> appels = appelVocalService.getAppelsCooperative(cooperativeId);
            
            return ResponseEntity.ok(Response.builder()
                    .responseCode("200")
                    .responseMessage("Appels de la coopérative récupérés avec succès")
                    .data(appels)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.builder()
                    .responseCode("400")
                    .responseMessage("Erreur : " + e.getMessage())
                    .build());
        }
    }
    
    /**
     * Compter les appels manqués
     */
    @GetMapping("/manques/count")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> compterAppelsManques(@RequestHeader("userId") Long femmeId) {
        try {
            Long count = appelVocalService.compterAppelsManques(femmeId);
            
            return ResponseEntity.ok(Response.builder()
                    .responseCode("200")
                    .responseMessage("Nombre d'appels manqués")
                    .data(count)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.builder()
                    .responseCode("400")
                    .responseMessage("Erreur : " + e.getMessage())
                    .build());
        }
    }
}