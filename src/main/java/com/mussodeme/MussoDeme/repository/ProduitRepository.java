package com.mussodeme.MussoDeme.repository;

import com.mussodeme.MussoDeme.entities.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduitRepository extends JpaRepository<Produit,Long> {
}
