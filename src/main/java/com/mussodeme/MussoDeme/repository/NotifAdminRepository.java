package com.mussodeme.MussoDeme.repository;

import com.mussodeme.MussoDeme.entities.NotifAdmin;
import com.mussodeme.MussoDeme.entities.Admin;
import com.mussodeme.MussoDeme.enums.TypeNotif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NotifAdminRepository extends JpaRepository<NotifAdmin, Long> {
    
    // Trouver toutes les notifications d'un admin
    List<NotifAdmin> findByAdmin(Admin admin);
    
    // Trouver toutes les notifications d'un admin par ID
    List<NotifAdmin> findByAdminId(Long adminId);
    
    // Trouver les notifications non lues d'un admin
    List<NotifAdmin> findByAdminAndStatusFalse(Admin admin);
    
    // Trouver les notifications non lues d'un admin par ID
    List<NotifAdmin> findByAdminIdAndStatusFalse(Long adminId);
    
    // Trouver les notifications lues d'un admin
    List<NotifAdmin> findByAdminAndStatusTrue(Admin admin);
    
    // Trouver les notifications par type
    List<NotifAdmin> findByTypeNotif(TypeNotif typeNotif);
    
    // Trouver les notifications d'un admin par type
    List<NotifAdmin> findByAdminAndTypeNotif(Admin admin, TypeNotif typeNotif);
    
    // Trouver les notifications d'un admin par type et ID
    List<NotifAdmin> findByAdminIdAndTypeNotif(Long adminId, TypeNotif typeNotif);
    
    // Compter les notifications non lues d'un admin
    @Query("SELECT COUNT(n) FROM NotifAdmin n WHERE n.admin = :admin AND n.status = false")
    Long countUnreadNotifications(@Param("admin") Admin admin);
    
    // Compter les notifications non lues d'un admin par ID
    @Query("SELECT COUNT(n) FROM NotifAdmin n WHERE n.admin.id = :adminId AND n.status = false")
    Long countUnreadNotificationsByAdminId(@Param("adminId") Long adminId);
    
    // Marquer toutes les notifications comme lues pour un admin
    @Modifying
    @Transactional
    @Query("UPDATE NotifAdmin n SET n.status = true WHERE n.admin.id = :adminId AND n.status = false")
    void markAllAsReadForAdmin(@Param("adminId") Long adminId);
    
    // Trouver les dernières notifications d'un admin (limitées)
    @Query("SELECT n FROM NotifAdmin n WHERE n.admin = :admin ORDER BY n.dateNotif DESC")
    List<NotifAdmin> findRecentNotifications(@Param("admin") Admin admin);
    
    // Supprimer toutes les notifications d'un admin
    @Modifying
    @Transactional
    @Query("DELETE FROM NotifAdmin n WHERE n.admin.id = :adminId")
    void deleteByAdminId(@Param("adminId") Long adminId);
}