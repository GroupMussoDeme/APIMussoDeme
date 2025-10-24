package com.mussodeme.MussoDeme.repository;

import com.mussodeme.MussoDeme.entities.Historique;
import com.mussodeme.MussoDeme.entities.Utilisateur;
import com.mussodeme.MussoDeme.enums.TypeHistoriques;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistoriqueRepository extends JpaRepository<Historique, Long> {
    
    /**
     * Trouver tous les historiques d'un utilisateur
     */
    @Query("SELECT h FROM Historique h WHERE h.utilisateur = :utilisateur ORDER BY h.dateAction DESC")
    List<Historique> findByUtilisateur(@Param("utilisateur") Utilisateur utilisateur);
    
    /**
     * Trouver les historiques par type
     */
    @Query("SELECT h FROM Historique h WHERE h.typeHistoriques = :type ORDER BY h.dateAction DESC")
    List<Historique> findByType(@Param("type") TypeHistoriques type);
    
    /**
     * Trouver les historiques d'un utilisateur par type
     */
    @Query("SELECT h FROM Historique h WHERE h.utilisateur = :utilisateur AND h.typeHistoriques = :type ORDER BY h.dateAction DESC")
    List<Historique> findByUtilisateurAndType(
        @Param("utilisateur") Utilisateur utilisateur,
        @Param("type") TypeHistoriques type
    );
    
    /**
     * Trouver les historiques entre deux dates
     */
    @Query("SELECT h FROM Historique h WHERE h.dateAction BETWEEN :debut AND :fin ORDER BY h.dateAction DESC")
    List<Historique> findByDateBetween(
        @Param("debut") LocalDateTime debut,
        @Param("fin") LocalDateTime fin
    );
    
    /**
     * Trouver les historiques d'un utilisateur entre deux dates
     */
    @Query("SELECT h FROM Historique h WHERE h.utilisateur = :utilisateur " +
           "AND h.dateAction BETWEEN :debut AND :fin ORDER BY h.dateAction DESC")
    List<Historique> findByUtilisateurAndDateBetween(
        @Param("utilisateur") Utilisateur utilisateur,
        @Param("debut") LocalDateTime debut,
        @Param("fin") LocalDateTime fin
    );
    
    /**
     * Compter les historiques par type pour un utilisateur
     */
    @Query("SELECT COUNT(h) FROM Historique h WHERE h.utilisateur = :utilisateur AND h.typeHistoriques = :type")
    Long countByUtilisateurAndType(
        @Param("utilisateur") Utilisateur utilisateur,
        @Param("type") TypeHistoriques type
    );
}
