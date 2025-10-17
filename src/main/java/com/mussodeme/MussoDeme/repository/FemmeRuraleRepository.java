package com.mussodeme.MussoDeme.repository;

import com.mussodeme.MussoDeme.entities.FemmeRurale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FemmeRuraleRepository extends JpaRepository<FemmeRurale, Long> {
    Optional<FemmeRurale> findByNumeroTel(String numeroTel);
}
