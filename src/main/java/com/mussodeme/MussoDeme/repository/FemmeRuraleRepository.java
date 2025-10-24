package com.mussodeme.MussoDeme.repository;

import com.mussodeme.MussoDeme.entities.FemmeRurale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FemmeRuraleRepository extends JpaRepository<FemmeRurale, Long> {
    Optional<FemmeRurale> findByNumeroTel(String numeroTel);
    boolean existsByNumeroTel(String numeroTel);
    
    @Query("SELECT f FROM FemmeRurale f WHERE f.localite = :localite")
    List<FemmeRurale> findByLocalite(@Param("localite") String localite);
}
