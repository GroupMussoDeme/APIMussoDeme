package com.mussodeme.MussoDeme.repository;

import com.mussodeme.MussoDeme.entities.Notification;
import com.mussodeme.MussoDeme.entities.Utilisateur;
import com.mussodeme.MussoDeme.enums.TypeNotif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    // Trouver toutes les notifications d'un utilisateur
    List<Notification> findByUtilisateur(Utilisateur utilisateur);
    
    // Trouver toutes les notifications d'un utilisateur par ID
    List<Notification> findByUtilisateurId(Long utilisateurId);
    
    // Trouver les notifications non lues d'un utilisateur
    List<Notification> findByUtilisateurAndStatusFalse(Utilisateur utilisateur);
    
    // Trouver les notifications non lues d'un utilisateur par ID
    List<Notification> findByUtilisateurIdAndStatusFalse(Long utilisateurId);
    
    // Trouver les notifications lues d'un utilisateur
    List<Notification> findByUtilisateurAndStatusTrue(Utilisateur utilisateur);
    
    // Trouver les notifications par type
    List<Notification> findByTypeNotif(TypeNotif typeNotif);
    
    // Trouver les notifications d'un utilisateur par type
    List<Notification> findByUtilisateurAndTypeNotif(Utilisateur utilisateur, TypeNotif typeNotif);
    
    // Trouver les notifications d'un utilisateur par type et ID
    List<Notification> findByUtilisateurIdAndTypeNotif(Long utilisateurId, TypeNotif typeNotif);
    
    // Compter les notifications non lues d'un utilisateur
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.utilisateur = :utilisateur AND n.status = false")
    Long countUnreadNotifications(@Param("utilisateur") Utilisateur utilisateur);
    
    // Compter les notifications non lues d'un utilisateur par ID
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.utilisateur.id = :utilisateurId AND n.status = false")
    Long countUnreadNotificationsByUserId(@Param("utilisateurId") Long utilisateurId);
    
    // Marquer toutes les notifications comme lues pour un utilisateur
    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.status = true WHERE n.utilisateur.id = :utilisateurId AND n.status = false")
    void markAllAsReadForUser(@Param("utilisateurId") Long utilisateurId);
    
    // Trouver les dernières notifications d'un utilisateur (limitées)
    @Query("SELECT n FROM Notification n WHERE n.utilisateur = :utilisateur ORDER BY n.dateNotif DESC")
    List<Notification> findRecentNotifications(@Param("utilisateur") Utilisateur utilisateur);
    
    // Supprimer toutes les notifications d'un utilisateur
    @Modifying
    @Transactional
    @Query("DELETE FROM Notification n WHERE n.utilisateur.id = :utilisateurId")
    void deleteByUtilisateurId(@Param("utilisateurId") Long utilisateurId);
}