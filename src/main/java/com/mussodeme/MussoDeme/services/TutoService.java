package com.mussodeme.MussoDeme.services;

import com.mussodeme.MussoDeme.dto.TutoDTO;
import com.mussodeme.MussoDeme.entities.Admin;
import com.mussodeme.MussoDeme.entities.Tuto;
import com.mussodeme.MussoDeme.exceptions.NotFoundException;
import com.mussodeme.MussoDeme.repository.TutoRepository;
import com.mussodeme.MussoDeme.repository.UtilisateursRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TutoService {

    private final TutoRepository tutoRepository;
    private final UtilisateursRepository utilisateursRepository;

    private final String uploadDir = "uploads/tutos/";

    // ------------------ CREATE / UPLOAD ------------------
    public TutoDTO uploadTuto(MultipartFile file, TutoDTO dto) throws IOException {
        Admin admin = utilisateursRepository.findById(dto.getAdminId())
                .filter(u -> u instanceof Admin)
                .map(u -> (Admin) u)
                .orElseThrow(() -> new NotFoundException("Admin non trouvé"));

        // Crée le dossier si inexistant
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        // Sauvegarde le fichier
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);
        Files.copy(file.getInputStream(), filePath);

        // Enregistre en base
        Tuto tuto = Tuto.builder()
                .admin(admin)
                .titre(dto.getTitre())
                .langue(dto.getLangue())
                .description(dto.getDescription())
                .videoUrl(fileName)
                .duree(dto.getDuree())
                .build();

        tutoRepository.save(tuto);

        dto.setId(tuto.getId());
        dto.setAdminId(admin.getId());
        dto.setVideoUrl(fileName);

        return dto;
    }

    // ------------------ LIST ------------------
    public List<TutoDTO> listTutos() {
        return tutoRepository.findAll().stream()
                .map(t -> {
                    TutoDTO dto = new TutoDTO();
                    dto.setId(t.getId());
                    dto.setTitre(t.getTitre());
                    dto.setLangue(t.getLangue());
                    dto.setDescription(t.getDescription());
                    dto.setDuree(t.getDuree());
                    dto.setVideoUrl(t.getVideoUrl());
                    dto.setAdminId(t.getAdmin().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // ------------------ DOWNLOAD ------------------
    public Resource downloadTuto(Long id) throws MalformedURLException {
        Tuto tuto = tutoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tuto non trouvé"));

        Path path = Paths.get(uploadDir, tuto.getVideoUrl());
        return new UrlResource(path.toUri());
    }

    // ------------------ GET BY ID ------------------
    public TutoDTO getTuto(Long id) {
        Tuto t = tutoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tuto non trouvé"));
        TutoDTO dto = new TutoDTO();
        dto.setId(t.getId());
        dto.setTitre(t.getTitre());
        dto.setLangue(t.getLangue());
        dto.setDescription(t.getDescription());
        dto.setDuree(t.getDuree());
        dto.setVideoUrl(t.getVideoUrl());
        dto.setAdminId(t.getAdmin().getId());
        return dto;
    }

    // ------------------ UPDATE ------------------
    public TutoDTO updateTuto(Long id, TutoDTO dto) {
        Tuto t = tutoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tuto non trouvé"));

        t.setTitre(dto.getTitre());
        t.setLangue(dto.getLangue());
        t.setDescription(dto.getDescription());
        t.setDuree(dto.getDuree());

        tutoRepository.save(t);

        dto.setId(t.getId());
        dto.setAdminId(t.getAdmin().getId());
        dto.setVideoUrl(t.getVideoUrl());
        return dto;
    }

    // ------------------ DELETE ------------------
    public void deleteTuto(Long id) throws IOException {
        Tuto t = tutoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tuto non trouvé"));

        // Supprime le fichier
        Path path = Paths.get(uploadDir, t.getVideoUrl());
        Files.deleteIfExists(path);

        // Supprime en base
        tutoRepository.delete(t);
    }
}
