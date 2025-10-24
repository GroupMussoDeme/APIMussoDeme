package com.mussodeme.MussoDeme.repository;

import com.mussodeme.MussoDeme.entities.Utilisateur;
import com.mussodeme.MussoDeme.entities.UtilisateurAudio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UtilisateurAudioRepository extends JpaRepository<UtilisateurAudio, Long> {
    List<UtilisateurAudio> findByUtilisateur(Utilisateur utilisateur);
}
