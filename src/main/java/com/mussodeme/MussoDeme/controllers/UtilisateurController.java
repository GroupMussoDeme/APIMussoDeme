package com.mussodeme.MussoDeme.controllers;

import com.mussodeme.MussoDeme.dto.CreateUtilisateurRequest;
import com.mussodeme.MussoDeme.dto.UpdateUtilisateurRequest;
import com.mussodeme.MussoDeme.dto.UtilisateurDTO;
import com.mussodeme.MussoDeme.services.UtilisateurService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    // ==================== CRUD OPERATIONS ====================

    /**
     * Create a new user
     */
    @PostMapping
    public ResponseEntity<UtilisateurDTO> createUser(@Valid @RequestBody CreateUtilisateurRequest request) {
        try {
            UtilisateurDTO createdUser = utilisateurService.createUser(request);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            throw e; // Let the global exception handler handle this
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création de l'utilisateur: " + e.getMessage());
        }
    }

    /**
     * Get user by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurDTO> getUserById(@PathVariable Long id) {
        try {
            UtilisateurDTO user = utilisateurService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            throw new RuntimeException("Utilisateur non trouvé: " + e.getMessage());
        }
    }

    /**
     * Get all users
     */
    @GetMapping
    public ResponseEntity<List<UtilisateurDTO>> getAllUsers() {
        List<UtilisateurDTO> users = utilisateurService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Update user
     */
    @PutMapping("/{id}")
    public ResponseEntity<UtilisateurDTO> updateUser(@PathVariable Long id, 
                                                   @Valid @RequestBody UpdateUtilisateurRequest request) {
        try {
            UtilisateurDTO updatedUser = utilisateurService.updateUser(id, request);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            throw e; // Let the global exception handler handle this
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'utilisateur: " + e.getMessage());
        }
    }

    /**
     * Delete user
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            utilisateurService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression de l'utilisateur: " + e.getMessage());
        }
    }

    // ==================== ACTIVATION / DÉSACTIVATION ====================

    /**
     * Activer un utilisateur seulement s'il est désactivé
     */
    @PatchMapping("/{id}/activer")
    public ResponseEntity<UtilisateurDTO> activerUtilisateur(@PathVariable Long id) {
        try {
            UtilisateurDTO utilisateur = utilisateurService.activerUtilisateur(id);
            return ResponseEntity.ok(utilisateur);
        } catch (IllegalStateException e) {
            // L'utilisateur est déjà actif
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'activation de l'utilisateur: " + e.getMessage());
        }
    }

    /**
     * Désactiver un utilisateur seulement s'il est actif
     */
    @PatchMapping("/{id}/desactiver")
    public ResponseEntity<UtilisateurDTO> desactiverUtilisateur(@PathVariable Long id) {
        try {
            UtilisateurDTO utilisateur = utilisateurService.desactiverUtilisateur(id);
            return ResponseEntity.ok(utilisateur);
        } catch (IllegalStateException e) {
            // L'utilisateur est déjà inactif
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la désactivation de l'utilisateur: " + e.getMessage());
        }
    }

    // ==================== EXISTING FILE OPERATIONS ====================

    // Upload audio
    @PostMapping("/{id}/upload-audio")
    public String uploadAudio(@PathVariable Long id, @RequestParam("audio") MultipartFile file) {
        return utilisateurService.telechargerAudio(id, file);
    }

    // Upload vidéo
    @PostMapping("/{id}/upload-video")
    public String uploadVideo(@PathVariable Long id, @RequestParam("video") MultipartFile file) {
        return utilisateurService.telechargerVideo(id, file);
    }

    // Liste des audios
    @GetMapping("/audios")
    public List<String> getAudios() {
        return utilisateurService.listerAudios();
    }

    // Liste des vidéos
    @GetMapping("/videos")
    public List<String> getVideos() {
        return utilisateurService.listerVideos();
    }

    // Pictogrammes
    @GetMapping("/pictogrammes")
    public Map<String, String> getPictogrammes() {
        return utilisateurService.getPictogrammes();
    }

    // Navigation pictogramme
    @GetMapping("/{id}/naviguer")
    public String naviguer(@PathVariable Long id, @RequestParam String action) {
        return utilisateurService.naviguerParPictogramme(id, action);
    }
    
    // Upload d'image de profil
    @PostMapping(value = "/{id}/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadProfileImage(@PathVariable Long id, @RequestParam("image") MultipartFile file) {
        try {
            String result = utilisateurService.telechargerImageProfil(id, file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors du téléchargement de l'image: " + e.getMessage());
        }
    }
}