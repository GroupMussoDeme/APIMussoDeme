package com.mussodeme.MussoDeme.repository;

import com.mussodeme.MussoDeme.entities.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaiementRepository extends JpaRepository<Paiement,Long> {
    @Query("SELECT p FROM Paiement p WHERE p.acheteur.id = :acheteurId")
    List<Paiement> findByAcheteurId(@Param("acheteurId") Long acheteurId);
}
