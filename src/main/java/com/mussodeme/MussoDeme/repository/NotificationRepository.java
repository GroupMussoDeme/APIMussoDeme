package com.mussodeme.MussoDeme.repository;

import com.mussodeme.MussoDeme.entities.Notification;
import com.mussodeme.MussoDeme.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    List<Notification> findByUtilisateur(Utilisateur utilisateur);
}
