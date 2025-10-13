package com.mussodeme.MussoDeme.repository;

import com.mussodeme.MussoDeme.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilisateursRepository extends JpaRepository<Utilisateur,Long> {
    Optional<Utilisateur> findByNumeroTel(String numeroTel);
}
