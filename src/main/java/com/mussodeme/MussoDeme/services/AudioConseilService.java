package com.mussodeme.MussoDeme.services;

import com.mussodeme.MussoDeme.dto.AudioConseilDTO;
import com.mussodeme.MussoDeme.entities.Admin;
import com.mussodeme.MussoDeme.entities.AudioConseil;
import com.mussodeme.MussoDeme.exceptions.NotFoundException;
import com.mussodeme.MussoDeme.repository.AdminRepository;
import com.mussodeme.MussoDeme.repository.AudioConseilRepository;
import com.mussodeme.MussoDeme.repository.CategorieRepository;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AudioConseilService {

    private final AudioConseilRepository audioRepo;
    private final CategorieRepository categorieRepo;
    private final AdminRepository adminRepo;

    private final Path audioStorage = Paths.get("uploads/audio");

    // Upload audio (Admin)
    public AudioConseil uploadAudio(MultipartFile file, AudioConseilDTO dto) throws IOException {
        Admin admin = adminRepo.findById(dto.getAdminId())
                .orElseThrow(() -> new NotFoundException("Admin non trouvé"));

        var categorie = categorieRepo.findById(dto.getCategorieId())
                .orElseThrow(() -> new NotFoundException("Catégorie non trouvée"));

        if (!Files.exists(audioStorage)) Files.createDirectories(audioStorage);

        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path targetPath = audioStorage.resolve(filename);
        Files.write(targetPath, file.getBytes());

        AudioConseil audio = AudioConseil.builder()
                .titre(dto.getTitre())
                .langue(dto.getLangue())
                .description(dto.getDescription())
                .urlAudio(targetPath.toString())
                .imageUrl(dto.getImageUrl())
                .duree(dto.getDuree())
                .categorie(categorie)
                .admin(admin)
                .build();

        return audioRepo.save(audio);
    }

    // Liste tous les audios (FemmeRurale)
    public List<AudioConseilDTO> listAudios() {
        return audioRepo.findAll().stream().map(audio -> {
            AudioConseilDTO dto = new AudioConseilDTO();
            dto.setId(audio.getId());
            dto.setTitre(audio.getTitre());
            dto.setLangue(audio.getLangue());
            dto.setDescription(audio.getDescription());
            dto.setDuree(audio.getDuree());
            dto.setImageUrl(audio.getImageUrl());
            dto.setUrlAudio(audio.getUrlAudio());
            dto.setCategorieId(audio.getCategorie().getId());
            dto.setAdminId(audio.getAdmin().getId());
            return dto;
        }).toList();
    }

    // Télécharger un audio
    public Resource downloadAudio(Long audioId) throws IOException {
        AudioConseil audio = audioRepo.findById(audioId)
                .orElseThrow(() -> new NotFoundException("Audio non trouvé"));
        Path path = Paths.get(audio.getUrlAudio());
        if (!Files.exists(path)) throw new NotFoundException("Fichier introuvable");
        return new UrlResource(path.toUri());
    }
}

