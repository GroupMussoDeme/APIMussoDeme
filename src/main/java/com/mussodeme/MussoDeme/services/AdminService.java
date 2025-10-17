package com.mussodeme.MussoDeme.services;

import com.mussodeme.MussoDeme.dto.AudioConseilDTO;
import com.mussodeme.MussoDeme.dto.TutoDTO;
import com.mussodeme.MussoDeme.entities.*;
import com.mussodeme.MussoDeme.exceptions.NotFoundException;
import com.mussodeme.MussoDeme.repository.AudioConseilRepository;
import com.mussodeme.MussoDeme.repository.CategorieRepository;
import com.mussodeme.MussoDeme.repository.TutoRepository;
import com.mussodeme.MussoDeme.repository.UtilisateursRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UtilisateursRepository utilisateursRepository;
    private final AudioConseilRepository audioConseilRepository;
    private final TutoRepository tutoRepository;
    private final CategorieRepository categorieRepository;
    private final FileStorageService fileStorageService;

    // -------------------- AJOUTER UN AUDIO --------------------
    public AudioConseil addAudio(Long adminId, AudioConseilDTO dto, MultipartFile audioFile, MultipartFile imageFile) {
        Admin admin = (Admin) utilisateursRepository.findById(adminId)
                .orElseThrow(() -> new NotFoundException("Admin non trouvé"));

        Categorie categorie = categorieRepository.findById(dto.getCategorieId())
                .orElseThrow(() -> new NotFoundException("Catégorie introuvable"));

        // Sauvegarder les fichiers
        String audioUrl = fileStorageService.saveFile(audioFile, "audio");
        String imageUrl = imageFile != null ? fileStorageService.saveFile(imageFile, "image") : null;

        AudioConseil audio = AudioConseil.builder()
                .titre(dto.getTitre())
                .langue(dto.getLangue())
                .description(dto.getDescription())
                .urlAudio(audioUrl)
                .imageUrl(imageUrl)
                .duree(dto.getDuree())
                .categorie(categorie)
                .utilisateur(admin)
                .build();

        return audioConseilRepository.save(audio);
    }

    // -------------------- AJOUTER UN TUTO --------------------
    public Tuto addTuto(Long adminId, TutoDTO dto, MultipartFile videoFile) {
        Admin admin = (Admin) utilisateursRepository.findById(adminId)
                .orElseThrow(() -> new NotFoundException("Admin non trouvé"));

        String videoUrl = fileStorageService.saveFile(videoFile, "video");

        Tuto tuto = Tuto.builder()
                .titre(dto.getTitre())
                .langue(dto.getLangue())
                .description(dto.getDescription())
                .duree(dto.getDuree())
                .videoUrl(videoUrl)
                .admin(admin)
                .build();

        return tutoRepository.save(tuto);
    }

    // -------------------- SUPPRIMER UN AUDIO --------------------
    public void deleteAudio(Long id) {
        if (!audioConseilRepository.existsById(id)) {
            throw new NotFoundException("Audio introuvable");
        }
        audioConseilRepository.deleteById(id);
    }

    // -------------------- SUPPRIMER UN TUTO --------------------
    public void deleteTuto(Long id) {
        if (!tutoRepository.existsById(id)) {
            throw new NotFoundException("Tuto introuvable");
        }
        tutoRepository.deleteById(id);
    }
}
