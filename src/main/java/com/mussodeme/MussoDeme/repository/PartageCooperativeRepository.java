package com.mussodeme.MussoDeme.repository;

import com.mussodeme.MussoDeme.entities.Contenu;
import com.mussodeme.MussoDeme.entities.Coperative;
import com.mussodeme.MussoDeme.entities.FemmeRurale;
import com.mussodeme.MussoDeme.entities.PartageCooperative;
import com.mussodeme.MussoDeme.enums.TypeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartageCooperativeRepository extends JpaRepository<PartageCooperative, Long> {

    /**
     * Find all content shared in a cooperative
     */
    @Query("SELECT p FROM PartageCooperative p WHERE p.coperative = :coperative ORDER BY p.datePartage DESC")
    List<PartageCooperative> findByCooperative(@Param("coperative") Coperative coperative);

    /**
     * Find all shares of a specific content
     */
    @Query("SELECT p FROM PartageCooperative p WHERE p.contenu = :contenu ORDER BY p.datePartage DESC")
    List<PartageCooperative> findByContenu(@Param("contenu") Contenu contenu);

    /**
     * Find all content shared by a specific user
     */
    @Query("SELECT p FROM PartageCooperative p WHERE p.partagePar = :user ORDER BY p.datePartage DESC")
    List<PartageCooperative> findByUser(@Param("user") FemmeRurale user);

    /**
     * Find shared content in a cooperative filtered by TypeInfo
     */
    @Query("SELECT p FROM PartageCooperative p WHERE p.coperative = :coperative " +
           "AND p.contenu.typeInfo = :typeInfo ORDER BY p.datePartage DESC")
    List<PartageCooperative> findByCooperativeAndTypeInfo(@Param("coperative") Coperative coperative,
                                                           @Param("typeInfo") TypeInfo typeInfo);

    /**
     * Check if content is already shared in a cooperative
     */
    @Query("SELECT COUNT(p) > 0 FROM PartageCooperative p WHERE p.coperative = :coperative AND p.contenu = :contenu")
    boolean existsByCooperativeAndContenu(@Param("coperative") Coperative coperative,
                                          @Param("contenu") Contenu contenu);

    /**
     * Get recent shares in a cooperative (last N shares)
     */
    @Query("SELECT p FROM PartageCooperative p WHERE p.coperative = :coperative ORDER BY p.datePartage DESC")
    List<PartageCooperative> findRecentSharesByCooperative(@Param("coperative") Coperative coperative);
    
    // Méthodes manquantes
    List<PartageCooperative> findByCoperativeId(Long coperativeId);
    
    @Query("SELECT p FROM PartageCooperative p WHERE p.coperative.id = :coperativeId AND p.contenu.typeInfo = :typeInfo")
    List<PartageCooperative> findByCoperativeIdAndContenu_TypeInfo(@Param("coperativeId") Long coperativeId, 
                                                                   @Param("typeInfo") TypeInfo typeInfo);
}
