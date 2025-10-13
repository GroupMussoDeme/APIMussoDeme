package com.mussodeme.MussoDeme.repository;

import com.mussodeme.MussoDeme.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
}
