package com.mussodeme.MussoDeme.repository;

import com.mussodeme.MussoDeme.entities.Historique;
import com.mussodeme.MussoDeme.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoriqueRepository extends JpaRepository<Historique,Long> {
    List<Historique> findByUtilisateur(Utilisateur utilisateur);
}
