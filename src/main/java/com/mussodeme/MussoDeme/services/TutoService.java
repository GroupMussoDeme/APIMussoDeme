package com.mussodeme.MussoDeme.services;

import com.mussodeme.MussoDeme.dto.TutoDTO;
import com.mussodeme.MussoDeme.entities.Admin;
import com.mussodeme.MussoDeme.entities.Tuto;
import com.mussodeme.MussoDeme.exceptions.NotFoundException;
import com.mussodeme.MussoDeme.repository.AdminRepository;
import com.mussodeme.MussoDeme.repository.TutoRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TutoService {

    private final TutoRepository tutoRepo;
    private final AdminRepository adminRepo;
    private final Path videoStorage = Paths.get("uploads/video");

    // Upload vidéo (Admin)
    public Tuto uploadTuto(MultipartFile file, TutoDTO dto) throws IOException {
        Admin admin = adminRepo.findById(dto.getAdminId())
                .orElseThrow(() -> new NotFoundException("Admin non trouvé"));

        if (!Files.exists(videoStorage)) Files.createDirectories(videoStorage);

        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path targetPath = videoStorage.resolve(filename);
        Files.write(targetPath, file.getBytes());

        Tuto tuto = Tuto.builder()
                .titre(dto.getTitre())
                .langue(dto.getLangue())
                .description(dto.getDescription())
                .videoUrl(targetPath.toString())
                .duree(dto.getDuree())
                .admin(admin)
                .build();

        return tutoRepo.save(tuto);
    }

    // Liste tous les tutos
    public List<TutoDTO> listTutos() {
        return tutoRepo.findAll().stream().map(tuto -> {
            TutoDTO dto = new TutoDTO();
            dto.setId(tuto.getId());
            dto.setTitre(tuto.getTitre());
            dto.setLangue(tuto.getLangue());
            dto.setDescription(tuto.getDescription());
            dto.setVideoUrl(tuto.getVideoUrl());
            dto.setDuree(tuto.getDuree());
            dto.setAdminId(tuto.getAdmin().getId());
            return dto;
        }).toList();
    }

    // Télécharger un tuto
    public Resource downloadTuto(Long tutoId) throws IOException {
        Tuto tuto = tutoRepo.findById(tutoId)
                .orElseThrow(() -> new NotFoundException("Tuto non trouvé"));
        Path path = Paths.get(tuto.getVideoUrl());
        if (!Files.exists(path)) throw new NotFoundException("Fichier introuvable");
        return new UrlResource(path.toUri());
    }
}

