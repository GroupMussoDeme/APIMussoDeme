package com.mussodeme.MussoDeme.repository;

import com.mussodeme.MussoDeme.entities.RechercherParLocalisation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RechercheParLocationRepository extends JpaRepository<RechercherParLocalisation,Long> {
}
