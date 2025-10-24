package com.mussodeme.MussoDeme.repository;

import com.mussodeme.MussoDeme.entities.AppelVocal;
import com.mussodeme.MussoDeme.entities.Coperative;
import com.mussodeme.MussoDeme.entities.FemmeRurale;
import com.mussodeme.MussoDeme.enums.StatutAppel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppelVocalRepository extends JpaRepository<AppelVocal, Long> {
    
    // Trouver les appels reçus par une femme
    List<AppelVocal> findByAppeleAndStatutIn(FemmeRurale appele, List<StatutAppel> statuts);
    
    // Trouver les appels émis par une femme
    List<AppelVocal> findByAppelant(FemmeRurale appelant);
    
    // Trouver les appels manqués
    List<AppelVocal> findByAppeleAndStatut(FemmeRurale appele, StatutAppel statut);
    
    // Trouver les appels d'une coopérative
    List<AppelVocal> findByCooperative(Coperative cooperative);
    
    // Trouver les appels non répondus
    @Query("SELECT a FROM AppelVocal a WHERE a.appele = :femme AND a.statut = 'MANQUE' ORDER BY a.dateAppel DESC")
    List<AppelVocal> findAppelsManques(@Param("femme") FemmeRurale femme);
    
    // Compter les appels manqués
    @Query("SELECT COUNT(a) FROM AppelVocal a WHERE a.appele = :femme AND a.statut = 'MANQUE'")
    Long countAppelsManques(@Param("femme") FemmeRurale femme);
    
    // Trouver les derniers appels
    @Query("SELECT a FROM AppelVocal a WHERE a.appele = :femme OR a.appelant = :femme ORDER BY a.dateAppel DESC")
    List<AppelVocal> findDerniersAppels(@Param("femme") FemmeRurale femme);
}