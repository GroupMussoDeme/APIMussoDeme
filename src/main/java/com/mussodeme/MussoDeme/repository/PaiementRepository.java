package com.mussodeme.MussoDeme.repository;

import com.mussodeme.MussoDeme.entities.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaiementRepository extends JpaRepository<Paiement,Long> {
}
