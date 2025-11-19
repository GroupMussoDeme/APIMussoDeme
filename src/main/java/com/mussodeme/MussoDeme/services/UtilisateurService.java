package com.mussodeme.MussoDeme.services;

import com.mussodeme.MussoDeme.dto.CreateUtilisateurRequest;
import com.mussodeme.MussoDeme.dto.UpdateUtilisateurRequest;
import com.mussodeme.MussoDeme.dto.UtilisateurDTO;
import com.mussodeme.MussoDeme.entities.FemmeRurale;
import com.mussodeme.MussoDeme.entities.Utilisateur;
import com.mussodeme.MussoDeme.repository.FemmeRuraleRepository;
import com.mussodeme.MussoDeme.repository.UtilisateursRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UtilisateurService {

    private final UtilisateursRepository utilisateursRepository;
    private final FemmeRuraleRepository femmeRuraleRepository;
    private final ModelMapper modelMapper;

    // Répertoires pour stocker les fichiers médias
    private static final String AUDIO_DIRECTORY = "uploads/audios/";
    private static final String VIDEO_DIRECTORY = "uploads/videos/";

    // Liste des pictogrammes de navigation
    private final Map<String, String> pictogrammes = new HashMap<>();

    public UtilisateurService(UtilisateursRepository utilisateursRepository, 
                             FemmeRuraleRepository femmeRuraleRepository,
                             ModelMapper modelMapper) {
        this.utilisateursRepository = utilisateursRepository;
        this.femmeRuraleRepository = femmeRuraleRepository;
        this.modelMapper = modelMapper;
        
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

    // ==================== CRUD OPERATIONS ====================

    /**
     * Create a new user
     */
    public UtilisateurDTO createUser(CreateUtilisateurRequest request) {
        // Check if user with this phone number already exists
        if (utilisateursRepository.existsByNumeroTel(request.getNumeroTel())) {
            throw new IllegalArgumentException("Un utilisateur avec ce numéro de téléphone existe déjà");
        }

        // Create the appropriate user type based on role
        Utilisateur utilisateur;
        if ("FEMME_RURALE".equals(request.getRole().name())) {
            FemmeRurale femmeRurale = new FemmeRurale();
            // Set common properties
            setUtilisateurProperties(femmeRurale, request);
            utilisateur = femmeRurale;
        } else {
            // For other user types, create a generic Utilisateur
            utilisateur = new FemmeRurale(); // Using FemmeRurale as default for now
            setUtilisateurProperties(utilisateur, request);
        }

        Utilisateur saved = utilisateursRepository.save(utilisateur);
        return modelMapper.map(saved, UtilisateurDTO.class);
    }

    /**
     * Get user by ID
     */
    public UtilisateurDTO getUserById(Long id) {
        Utilisateur utilisateur = utilisateursRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + id));
        return modelMapper.map(utilisateur, UtilisateurDTO.class);
    }

    /**
     * Get all users
     */
    public List<UtilisateurDTO> getAllUsers() {
        List<Utilisateur> utilisateurs = utilisateursRepository.findAll();
        return utilisateurs.stream()
                .map(u -> modelMapper.map(u, UtilisateurDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Update user
     */
    public UtilisateurDTO updateUser(Long id, UpdateUtilisateurRequest request) {
        Utilisateur utilisateur = utilisateursRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + id));

        // Update only the fields that are provided in the request
        if (request.getNom() != null) {
            utilisateur.setNom(request.getNom());
        }
        if (request.getPrenom() != null) {
            utilisateur.setPrenom(request.getPrenom());
        }
        if (request.getNumeroTel() != null) {
            // Check if another user already has this phone number
            if (!request.getNumeroTel().equals(utilisateur.getNumeroTel()) && 
                utilisateursRepository.existsByNumeroTel(request.getNumeroTel())) {
                throw new IllegalArgumentException("Un utilisateur avec ce numéro de téléphone existe déjà");
            }
            utilisateur.setNumeroTel(request.getNumeroTel());
        }
        if (request.getMotCle() != null) {
            utilisateur.setMotCle(request.getMotCle());
        }
        if (request.getLocalite() != null) {
            utilisateur.setLocalite(request.getLocalite());
        }
        if (request.getEmail() != null) {
            utilisateur.setEmail(request.getEmail());
        }
        if (request.getRole() != null) {
            utilisateur.setRole(request.getRole());
        }
        if (request.getActive() != null) {
            utilisateur.setActive(request.getActive());
        }

        Utilisateur updated = utilisateursRepository.save(utilisateur);
        return modelMapper.map(updated, UtilisateurDTO.class);
    }

    /**
     * Delete user
     */
    public void deleteUser(Long id) {
        if (!utilisateursRepository.existsById(id)) {
            throw new RuntimeException("Utilisateur non trouvé avec l'ID: " + id);
        }
        utilisateursRepository.deleteById(id);
    }

    /**
     * Helper method to set common properties for Utilisateur
     */
    private void setUtilisateurProperties(Utilisateur utilisateur, CreateUtilisateurRequest request) {
        utilisateur.setNom(request.getNom());
        utilisateur.setPrenom(request.getPrenom());
        utilisateur.setNumeroTel(request.getNumeroTel());
        utilisateur.setMotCle(request.getMotCle());
        utilisateur.setLocalite(request.getLocalite());
        utilisateur.setEmail(request.getEmail());
        utilisateur.setRole(request.getRole());
        utilisateur.setActive(request.isActive());
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