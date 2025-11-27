package com.mussodeme.MussoDeme.controllers;

import com.mussodeme.MussoDeme.dto.NotifAdminDTO;
import com.mussodeme.MussoDeme.dto.Response;
import com.mussodeme.MussoDeme.enums.TypeNotif;
import com.mussodeme.MussoDeme.services.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notif-admin")
public class  NotifAdminController {

    private final NotificationService notificationService;

    // Constructor for dependency injection
    public NotifAdminController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Récupérer toutes les notifications d'un admin
     */
    @GetMapping("/admin/{adminId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> getNotificationsAdmin(@PathVariable Long adminId) {
        try {
            List<NotifAdminDTO> notifications = notificationService.getNotificationsAdmin(adminId);
            
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
     * Récupérer les notifications non lues d'un admin
     */
    @GetMapping("/admin/{adminId}/non-lues")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> getNotificationsNonLues(@PathVariable Long adminId) {
        try {
            List<NotifAdminDTO> notifications = notificationService.getNotificationsAdminNonLues(adminId);
            
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
     * Compter les notifications non lues d'un admin
     */
    @GetMapping("/admin/{adminId}/count-non-lues")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> compterNotificationsNonLues(@PathVariable Long adminId) {
        try {
            Long count = notificationService.compterNotificationsAdminNonLues(adminId);
            
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
     * Marquer une notification admin comme lue
     */
    @PutMapping("/{notificationId}/marquer-lue")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> marquerCommeLue(@PathVariable Long notificationId) {
        try {
            NotifAdminDTO notification = notificationService.marquerNotificationAdminCommeLue(notificationId);
            
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
     * Marquer toutes les notifications d'un admin comme lues
     */
    @PutMapping("/admin/{adminId}/marquer-toutes-lues")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> marquerToutesCommeLues(@PathVariable Long adminId) {
        try {
            notificationService.marquerToutesNotificationsAdminCommeLues(adminId);
            
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
     * Supprimer une notification admin
     */
    @DeleteMapping("/{notificationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> supprimerNotification(@PathVariable Long notificationId) {
        try {
            notificationService.supprimerNotificationAdmin(notificationId);
            
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
     * Récupérer les notifications d'un admin par type
     */
    @GetMapping("/admin/{adminId}/type/{typeNotif}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> getNotificationsParType(
            @PathVariable Long adminId, 
            @PathVariable TypeNotif typeNotif) {
        try {
            List<NotifAdminDTO> notifications = notificationService.getNotificationsAdminParType(adminId, typeNotif);
            
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