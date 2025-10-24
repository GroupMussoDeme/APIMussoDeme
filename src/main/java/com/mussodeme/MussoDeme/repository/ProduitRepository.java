package com.mussodeme.MussoDeme.repository;

import com.mussodeme.MussoDeme.entities.FemmeRurale;
import com.mussodeme.MussoDeme.entities.Produit;
import com.mussodeme.MussoDeme.enums.TypeProduit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProduitRepository extends JpaRepository<Produit,Long> {
    
    /**
     * Trouver les produits d'une femme rurale
     */
    List<Produit> findByFemmeRuraleId(Long femmeRuraleId);
    
    /**
     * Recherche vocale : Trouver les produits par type
     */
    @Query("SELECT p FROM Produit p WHERE p.typeProduit = :type ORDER BY p.id DESC")
    List<Produit> findByTypeProduit(@Param("type") TypeProduit type);
    
    /**
     * Recherche vocale : Trouver les produits par type avec stock disponible
     */
    List<Produit> findByTypeProduitAndQuantiteGreaterThan(TypeProduit typeProduit, int quantite);
    
    /**
     * Recherche par nom (recherche vocale convertie en texte)
     */
    List<Produit> findByNomContainingIgnoreCase(String nom);
}