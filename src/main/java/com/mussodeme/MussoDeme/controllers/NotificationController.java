package com.mussodeme.MussoDeme.controllers;

import com.mussodeme.MussoDeme.dto.NotificationDTO;
import com.mussodeme.MussoDeme.dto.Response;
import com.mussodeme.MussoDeme.enums.TypeNotif;
import com.mussodeme.MussoDeme.services.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    // Constructor for dependency injection
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Récupérer toutes les notifications d'un utilisateur
     */
    @GetMapping("/utilisateur/{utilisateurId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FEMME_RURALE', 'ACHETEUR')")
    public ResponseEntity<Response> getNotificationsUtilisateur(@PathVariable Long utilisateurId) {
        try {
            List<NotificationDTO> notifications = notificationService.getNotificationsUtilisateur(utilisateurId);
            
            return ResponseEntity.ok(Response.builder()
                    .responseCode("200")
                    .responseMessage("Notifications récupérées avec succès")
                    .data(notifications)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.builder()
                    .responseCode("400")
                    .responseMessage("Erreur : " + e.getMessage())
                    .build());
        }
    }

    /**
     * Récupérer les notifications non lues d'un utilisateur
     */
    @GetMapping("/utilisateur/{utilisateurId}/non-lues")
    @PreAuthorize("hasAnyRole('ADMIN', 'FEMME_RURALE', 'ACHETEUR')")
    public ResponseEntity<Response> getNotificationsNonLues(@PathVariable Long utilisateurId) {
        try {
            List<NotificationDTO> notifications = notificationService.getNotificationsNonLues(utilisateurId);
            
            return ResponseEntity.ok(Response.builder()
                    .responseCode("200")
                    .responseMessage("Notifications non lues récupérées avec succès")
                    .data(notifications)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.builder()
                    .responseCode("400")
                    .responseMessage("Erreur : " + e.getMessage())
                    .build());
        }
    }

    /**
     * Compter les notifications non lues d'un utilisateur
     */
    @GetMapping("/utilisateur/{utilisateurId}/count-non-lues")
    @PreAuthorize("hasAnyRole('ADMIN', 'FEMME_RURALE', 'ACHETEUR')")
    public ResponseEntity<Response> compterNotificationsNonLues(@PathVariable Long utilisateurId) {
        try {
            Long count = notificationService.compterNotificationsNonLues(utilisateurId);
            
            return ResponseEntity.ok(Response.builder()
                    .responseCode("200")
                    .responseMessage("Nombre de notifications non lues")
                    .data(count)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.builder()
                    .responseCode("400")
                    .responseMessage("Erreur : " + e.getMessage())
                    .build());
        }
    }

    /**
     * Marquer une notification comme lue
     */
    @PutMapping("/{notificationId}/marquer-lue")
    @PreAuthorize("hasAnyRole('ADMIN', 'FEMME_RURALE', 'ACHETEUR')")
    public ResponseEntity<Response> marquerCommeLue(@PathVariable Long notificationId) {
        try {
            NotificationDTO notification = notificationService.marquerCommeLue(notificationId);
            
            return ResponseEntity.ok(Response.builder()
                    .responseCode("200")
                    .responseMessage("Notification marquée comme lue")
                    .data(notification)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.builder()
                    .responseCode("400")
                    .responseMessage("Erreur : " + e.getMessage())
                    .build());
        }
    }

    /**
     * Marquer toutes les notifications d'un utilisateur comme lues
     */
    @PutMapping("/utilisateur/{utilisateurId}/marquer-toutes-lues")
    @PreAuthorize("hasAnyRole('ADMIN', 'FEMME_RURALE', 'ACHETEUR')")
    public ResponseEntity<Response> marquerToutesCommeLues(@PathVariable Long utilisateurId) {
        try {
            notificationService.marquerToutesCommeLues(utilisateurId);
            
            return ResponseEntity.ok(Response.builder()
                    .responseCode("200")
                    .responseMessage("Toutes les notifications ont été marquées comme lues")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.builder()
                    .responseCode("400")
                    .responseMessage("Erreur : " + e.getMessage())
                    .build());
        }
    }

    /**
     * Supprimer une notification
     */
    @DeleteMapping("/{notificationId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FEMME_RURALE', 'ACHETEUR')")
    public ResponseEntity<Response> supprimerNotification(@PathVariable Long notificationId) {
        try {
            notificationService.supprimerNotification(notificationId);
            
            return ResponseEntity.ok(Response.builder()
                    .responseCode("200")
                    .responseMessage("Notification supprimée avec succès")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.builder()
                    .responseCode("400")
                    .responseMessage("Erreur : " + e.getMessage())
                    .build());
        }
    }

    /**
     * Récupérer les notifications d'un utilisateur par type
     */
    @GetMapping("/utilisateur/{utilisateurId}/type/{typeNotif}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FEMME_RURALE', 'ACHETEUR')")
    public ResponseEntity<Response> getNotificationsParType(
            @PathVariable Long utilisateurId, 
            @PathVariable TypeNotif typeNotif) {
        try {
            List<NotificationDTO> notifications = notificationService.getNotificationsParType(utilisateurId, typeNotif);
            
            return ResponseEntity.ok(Response.builder()
                    .responseCode("200")
                    .responseMessage("Notifications par type récupérées avec succès")
                    .data(notifications)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.builder()
                    .responseCode("400")
                    .responseMessage("Erreur : " + e.getMessage())
                    .build());
        }
    }
}