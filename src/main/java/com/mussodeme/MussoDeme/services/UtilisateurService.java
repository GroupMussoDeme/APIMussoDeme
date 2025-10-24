package com.mussodeme.MussoDeme.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Service
public class UtilisateurService {

    // Répertoires pour stocker les fichiers médias
    private static final String AUDIO_DIRECTORY = "uploads/audios/";
    private static final String VIDEO_DIRECTORY = "uploads/videos/";

    // Liste des pictogrammes de navigation
    private final Map<String, String> pictogrammes = new HashMap<>();

    public UtilisateurService() {
        // Création des répertoires de stockage s’ils n’existent pas
        new File(AUDIO_DIRECTORY).mkdirs();
        new File(VIDEO_DIRECTORY).mkdirs();

        // Pictogrammes disponibles
        pictogrammes.put("produits", "icons/produits.png");
        pictogrammes.put("commandes", "icons/commandes.png");
        pictogrammes.put("chat", "icons/chat.png");
        pictogrammes.put("cooperative", "icons/cooperative.png");
        pictogrammes.put("profil", "icons/profil.png");
        pictogrammes.put("videos", "icons/videos.png");
        pictogrammes.put("audios", "icons/audios.png");
    }

    // ====================  GESTION DES AUDIOS ====================

    public String telechargerAudio(Long utilisateurId, MultipartFile audioFile) {
        if (audioFile.isEmpty()) return "Aucun fichier audio reçu pour l’utilisateur " + utilisateurId;

        try {
            String fileName = "audio_user_" + utilisateurId + "_" + System.currentTimeMillis() + "_" + audioFile.getOriginalFilename();
            Path filePath = Paths.get(AUDIO_DIRECTORY + fileName);
            Files.copy(audioFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return "Fichier audio téléchargé avec succès : " + fileName;
        } catch (IOException e) {
            return "Erreur lors du téléchargement audio : " + e.getMessage();
        }
    }

    public List<String> listerAudios() {
        return listerFichiers(AUDIO_DIRECTORY);
    }

    // ==================== GESTION DES VIDÉOS ====================

    public String telechargerVideo(Long utilisateurId, MultipartFile videoFile) {
        if (videoFile.isEmpty()) return "Aucun fichier vidéo reçu pour l’utilisateur " + utilisateurId;

        try {
            String fileName = "video_user_" + utilisateurId + "_" + System.currentTimeMillis() + "_" + videoFile.getOriginalFilename();
            Path filePath = Paths.get(VIDEO_DIRECTORY + fileName);
            Files.copy(videoFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return "Fichier vidéo téléchargé avec succès : " + fileName;
        } catch (IOException e) {
            return "Erreur lors du téléchargement vidéo : " + e.getMessage();
        }
    }

    public List<String> listerVideos() {
        return listerFichiers(VIDEO_DIRECTORY);
    }

    // ==================== NAVIGATION PAR PICTOGRAMME ====================

    public Map<String, String> getPictogrammes() {
        return pictogrammes;
    }

    public String naviguerParPictogramme(Long utilisateurId, String action) {
        String pictogramme = pictogrammes.get(action.toLowerCase());
        if (pictogramme == null)
            return "Action inconnue : " + action;
        return "L’utilisateur " + utilisateurId + " navigue vers '" + action + "' via le pictogramme " + pictogramme;
    }

    // ==================== MÉTHODE UTILITAIRE ====================

    private List<String> listerFichiers(String directoryPath) {
        List<String> fichiers = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(directoryPath))) {
            for (Path path : stream) {
                fichiers.add(path.getFileName().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fichiers;
    }
}
