package com.mussodeme.MussoDeme.services;

import com.mussodeme.MussoDeme.entities.AudioConseil;
import com.mussodeme.MussoDeme.entities.Tuto;
import com.mussodeme.MussoDeme.repository.AudioConseilRepository;
import com.mussodeme.MussoDeme.repository.TutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UtilisateurService {

    private final AudioConseilRepository audioConseilRepository;
    private final TutoRepository tutoRepository;

    public List<AudioConseil> getAllAudios() {
        return audioConseilRepository.findAll();
    }

    public List<Tuto> getAllTutos() {
        return tutoRepository.findAll();
    }

    public AudioConseil getAudioById(Long id) {
        return audioConseilRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Audio non trouvé"));
    }

    public Tuto getTutoById(Long id) {
        return tutoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tuto non trouvé"));
    }
}
