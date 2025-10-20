package com.mussodeme.MussoDeme.repository;

import com.mussodeme.MussoDeme.entities.Categorie;
import com.mussodeme.MussoDeme.enums.TypeCategorie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategorieRepository extends JpaRepository<Categorie, Long> {
    List<Categorie> findByTypeCategorie(TypeCategorie typeCategorie);
}
