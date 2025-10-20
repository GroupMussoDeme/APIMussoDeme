package com.mussodeme.MussoDeme.repository;

import com.mussodeme.MussoDeme.entities.FemmeRurale;
import com.mussodeme.MussoDeme.entities.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProduitRepository extends JpaRepository<Produit,Long> {
    List<Produit> findByFemmeRurale(FemmeRurale femmeRurale);
}
