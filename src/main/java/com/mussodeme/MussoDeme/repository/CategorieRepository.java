package com.mussodeme.MussoDeme.repository;

import com.mussodeme.MussoDeme.entities.Categorie;
import com.mussodeme.MussoDeme.enums.TypeCategorie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategorieRepository extends JpaRepository<Categorie, Long> {
    // La méthode findByTypeCategorie(TypeCategorie typeCategorie) est incorrecte
    // car dans l'entité Categorie, typeCategorie est un String, pas un enum
    // On la supprime pour éviter les erreurs de compilation
}