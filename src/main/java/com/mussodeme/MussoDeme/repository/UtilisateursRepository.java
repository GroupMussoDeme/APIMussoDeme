package com.mussodeme.MussoDeme.repository;

import com.mussodeme.MussoDeme.entities.Admin;
import com.mussodeme.MussoDeme.entities.FemmeRurale;
import com.mussodeme.MussoDeme.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateursRepository extends JpaRepository<Utilisateur, Long> {

    //Trouver un utilisateur générique par numéro
    Optional<Utilisateur> findByNumeroTel(String numeroTel);

    boolean existsByNumeroTel(String numeroTel);

    //Requête pour trouver une Femme Rurale par numéro
    @Query("SELECT f FROM FemmeRurale f WHERE f.numeroTel = ?1")
    Optional<FemmeRurale> findFemmeRuraleByNumeroTel(String numeroTel);
    
    // Trouver tous les utilisateurs actifs
    List<Utilisateur> findByActiveTrue();
    
    // Trouver tous les utilisateurs inactifs
    List<Utilisateur> findByActiveFalse();
    
    // Trouver tous les utilisateurs triés par date de création (du plus récent au plus ancien)
    List<Utilisateur> findAllByOrderByCreatedAtDesc();
    
    // Trouver tous les utilisateurs triés par date de création (du plus ancien au plus récent)
    List<Utilisateur> findAllByOrderByCreatedAtAsc();
}
