package com.mussodeme.MussoDeme.repository;

import com.mussodeme.MussoDeme.entities.Commande;
import com.mussodeme.MussoDeme.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Long> {
    List<Commande> findByAcheteur(Utilisateur acheteur);
    
    @Query("SELECT c FROM Commande c WHERE c.acheteur.id = :acheteurId")
    List<Commande> findByAcheteurId(@Param("acheteurId") Long acheteurId);
    
    @Query("SELECT c FROM Commande c WHERE c.produit.femmeRurale.id = :femmeRuraleId")
    List<Commande> findByProduit_FemmeRurale_Id(@Param("femmeRuraleId") Long femmeRuraleId);
}
