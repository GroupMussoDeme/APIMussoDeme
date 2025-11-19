package com.mussodeme.MussoDeme.services;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;

@Service
public class FileStorageService {

    private final Path rootLocation = Paths.get("uploads");

    public FileStorageService() {
        try {
            Files.createDirectories(rootLocation.resolve("audios"));
            Files.createDirectories(rootLocation.resolve("videos"));
            Files.createDirectories(rootLocation.resolve("logos"));
        } catch (IOException e) {
            throw new RuntimeException("Impossible de créer les dossiers de stockage", e);
        }
    }

    // -------------------- Enregistrement fichier --------------------
    public String saveFile(MultipartFile file, String type) {
        try {
            String folder = type.equalsIgnoreCase("audio") ? "audios" : 
                           type.equalsIgnoreCase("logo") ? "logos" : "videos";
            Path destination = this.rootLocation.resolve(folder)
                    .resolve(file.getOriginalFilename());

            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            // Retourne l’URL d’accès au fichier
            return "/api/files/" + type + "/" + file.getOriginalFilename();

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l’enregistrement du fichier : " + e.getMessage());
        }
    }

    // -------------------- Lecture fichier --------------------
    public Resource loadFile(String filename, String type) {
        try {
            String folder = type.equals("audio") ? "audios" : 
                           type.equals("logo") ? "logos" : "videos";
            Path file = rootLocation.resolve(folder).resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Fichier illisible ou inexistant");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Erreur : " + e.getMessage());
        }
    }
}

