package com.mussodeme.MussoDeme.repository;

import com.mussodeme.MussoDeme.entities.Categorie;
import com.mussodeme.MussoDeme.entities.Contenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContenuRepository extends JpaRepository<Contenu, Long> {
    List<Contenu> findByCategorie(Categorie categorie);
}
