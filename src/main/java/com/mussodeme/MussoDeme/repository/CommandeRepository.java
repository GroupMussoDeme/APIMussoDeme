package com.mussodeme.MussoDeme.repository;

import com.mussodeme.MussoDeme.entities.Acheteur;
import com.mussodeme.MussoDeme.entities.Commande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Long> {
    List<Commande> findByAcheteur(Acheteur acheteur);
}
