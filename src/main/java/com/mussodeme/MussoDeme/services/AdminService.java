package com.mussodeme.MussoDeme.services;

import com.mussodeme.MussoDeme.dto.ContenuDTO;
import com.mussodeme.MussoDeme.entities.*;
import com.mussodeme.MussoDeme.exceptions.NotFoundException;
import com.mussodeme.MussoDeme.repository.AdminRepository;
import com.mussodeme.MussoDeme.repository.ContenuRepository;
import com.mussodeme.MussoDeme.repository.CategorieRepository;
import com.mussodeme.MussoDeme.repository.UtilisateursRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final ContenuRepository contenuRepository;
    private final CategorieRepository categorieRepository;
    private final FileStorageService fileStorageService;

    // -------------------- AJOUTER UN AUDIO --------------------
    public Contenu addAudio(Long adminId, ContenuDTO dto, MultipartFile audioFile, MultipartFile imageFile) {
        Admin admin = (Admin) adminRepository.findById(adminId)
                .orElseThrow(() -> new NotFoundException("Admin non trouvé"));

        Categorie categorie = categorieRepository.findById(dto.getCategorieId())
                .orElseThrow(() -> new NotFoundException("Catégorie introuvable"));

        // Sauvegarder les fichiers
        String audioUrl = fileStorageService.saveFile(audioFile, "audio");
        String imageUrl = imageFile != null ? fileStorageService.saveFile(imageFile, "image") : null;

        Contenu audio = Contenu.builder()
                .titre(dto.getTitre())
                .langue(dto.getLangue())
                .description(dto.getDescription())
                .urlContenu(audioUrl)
                .duree(dto.getDuree())
                .categorie(categorie)
                .admin(admin)
                .build();

        return contenuRepository.save(audio);
    }


    // -------------------- SUPPRIMER UN AUDIO --------------------
    public void deleteAudio(Long id) {
        if (!contenuRepository.existsById(id)) {
            throw new NotFoundException("Audio introuvable");
        }
        contenuRepository.deleteById(id);
    }

}
