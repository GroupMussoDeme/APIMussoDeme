package com.mussodeme.MussoDeme.services;

import com.mussodeme.MussoDeme.entities.Contenu;
import com.mussodeme.MussoDeme.repository.ContenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UtilisateurService {

    private final ContenuRepository contenuRepository;
    private final TutoRepository tutoRepository;

    public List<Contenu> getAllAudios() {
        return contenuRepository.findAll();
    }


    public Contenu getAudioById(Long id) {
        return contenuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Audio non trouvé"));
    }
}
