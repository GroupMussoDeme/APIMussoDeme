package com.mussodeme.MussoDeme.repository;

import com.mussodeme.MussoDeme.entities.Appartenance;
import com.mussodeme.MussoDeme.entities.Coperative;
import com.mussodeme.MussoDeme.entities.FemmeRurale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AppartenanceRepository extends JpaRepository<Appartenance,Long> {
    List<Appartenance> findByFemmeRurale(FemmeRurale femmeRurale);
    boolean existsByFemmeRuraleAndCoperative(FemmeRurale femmeRurale, Coperative coperative);
    
    // Méthodes manquantes
    boolean existsByCoperativeIdAndFemmeRuraleId(Long coperativeId, Long femmeRuraleId);
    Optional<Appartenance> findByCoperativeIdAndFemmeRuraleId(Long coperativeId, Long femmeRuraleId);
    
    @Query("SELECT a.femmeRurale FROM Appartenance a WHERE a.coperative.id = :coperativeId")
    List<FemmeRurale> findFemmeRuralesByCoperativeId(@Param("coperativeId") Long coperativeId);
}