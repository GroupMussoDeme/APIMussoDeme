package com.mussodeme.MussoDeme.repository;

import com.mussodeme.MussoDeme.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateursRepository extends JpaRepository<Utilisateur,Long> {
}
