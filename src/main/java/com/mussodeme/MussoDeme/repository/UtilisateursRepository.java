package com.mussodeme.MussoDeme.repository;

import com.mussodeme.MussoDeme.entities.Admin;
import com.mussodeme.MussoDeme.entities.Utilisateur;
import com.mussodeme.MussoDeme.entities.FemmeRurale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilisateursRepository extends JpaRepository<Utilisateur, Long> {

    // Rechercher un Admin par email
    Optional<Admin> findByEmail(String email);

    // Rechercher un utilisateur (Admin ou FemmeRurale) par numéro de téléphone
    Optional<FemmeRurale> findByNumeroTel(String numeroTel);
}
