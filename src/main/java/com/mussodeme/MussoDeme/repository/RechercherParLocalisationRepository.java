package com.mussodeme.MussoDeme.repository;

import com.mussodeme.MussoDeme.entities.RechercherParLocalisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RechercherParLocalisationRepository extends JpaRepository<RechercherParLocalisation, Long> {
    List<RechercherParLocalisation> findByAcheteurIdOrderByDateRechercheDesc(Long acheteurId);
}