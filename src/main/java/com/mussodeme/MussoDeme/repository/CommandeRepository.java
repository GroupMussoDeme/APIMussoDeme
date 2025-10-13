package com.mussodeme.MussoDeme.repository;

import com.mussodeme.MussoDeme.entities.Commande;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandeRepository extends JpaRepository<Commande, Long> {
}
