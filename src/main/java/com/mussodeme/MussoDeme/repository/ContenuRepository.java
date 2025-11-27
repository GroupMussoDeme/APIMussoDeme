package com.mussodeme.MussoDeme.repository;

import com.mussodeme.MussoDeme.entities.Categorie;
import com.mussodeme.MussoDeme.entities.Contenu;
import com.mussodeme.MussoDeme.enums.TypeCategorie;
import com.mussodeme.MussoDeme.enums.TypeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContenuRepository extends JpaRepository<Contenu, Long> {
    
    // Filtrer par type de catégorie (AUDIO, VIDEO, INSTITUTION_FINANCIERE)
    @Query("SELECT c FROM Contenu c WHERE c.categorie = :type")
    List<Contenu> findByCategorieType(@Param("type") TypeCategorie type);
    
    // Lister tous les contenus triés par date de création (plus récent d'abord)
    @Query("SELECT c FROM Contenu c ORDER BY c.id DESC")
    List<Contenu> findAllOrderByDateDesc();
    
    // Lister tous les contenus triés par date de création (plus ancien d'abord)
    @Query("SELECT c FROM Contenu c ORDER BY c.id ASC")
    List<Contenu> findAllOrderByDateAsc();
    
    // Lister les contenus avec durée (AUDIO et VIDEO uniquement)
    @Query("SELECT c FROM Contenu c WHERE c.duree IS NOT NULL AND c.duree != ''")
    List<Contenu> findAllWithDuration();
    
    // Filtrer par type ET avec durée
    @Query("SELECT c FROM Contenu c WHERE c.categorie = :type AND c.duree IS NOT NULL AND c.duree != ''")
    List<Contenu> findByCategorieTypeWithDuration(@Param("type") TypeCategorie type);
    
    // Filtrer par TypeInfo (SANTE, NUTRITION, DROIT, VIDEO_FORMATION)
    @Query("SELECT c FROM Contenu c WHERE c.typeInfo = :typeInfo")
    List<Contenu> findByTypeInfo(@Param("typeInfo") TypeInfo typeInfo);
    
    // Filtrer par TypeInfo et TypeCategorie combinés
    @Query("SELECT c FROM Contenu c WHERE c.typeInfo = :typeInfo AND c.categorie = :typeCategorie")
    List<Contenu> findByTypeInfoAndTypeCategorie(@Param("typeInfo") TypeInfo typeInfo, @Param("typeCategorie") TypeCategorie typeCategorie);
}