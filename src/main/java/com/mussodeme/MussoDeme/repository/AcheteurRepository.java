package com.mussodeme.MussoDeme.repository;

import com.mussodeme.MussoDeme.entities.Acheteur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AcheteurRepository extends JpaRepository<Acheteur, Long> {
    Optional<Acheteur> findByNumeroTel(String numeroTel);
    boolean existsByNumeroTel(String numeroTel);
}
