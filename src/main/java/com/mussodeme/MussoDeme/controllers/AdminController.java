package com.mussodeme.MussoDeme.controllers;

import com.mussodeme.MussoDeme.dto.AdminDTO;
import com.mussodeme.MussoDeme.dto.ContenuDTO;
import com.mussodeme.MussoDeme.dto.InstitutionFinanciereDTO;
import com.mussodeme.MussoDeme.dto.UpdateAdminRequest;
import com.mussodeme.MussoDeme.dto.UtilisateurDTO;
import com.mussodeme.MussoDeme.services.AdminService;
import com.mussodeme.MussoDeme.security.util.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private static final Logger logger = Logger.getLogger(AdminController.class.getName());

    private final AdminService adminService;
    private final JwtUtils jwtUtils;

    // Constructor for dependency injection
    public AdminController(AdminService adminService, JwtUtils jwtUtils) {
        this.adminService = adminService;
        this.jwtUtils = jwtUtils;
    }

    //===================== GESTION DU PROFIL ADMIN =====================
    
    /**
     * Récupérer son propre profil
     */
    @GetMapping("/profile/{id}")
    public ResponseEntity<AdminDTO> getProfile(@PathVariable Long id) {
        logger.info("Requête de récupération du profil admin: " + id);
        AdminDTO profile = adminService.getAdminProfile(id);
        return ResponseEntity.ok(profile);
    }
    
    /**
     * Récupérer un profil par email
     */
    @GetMapping("/profile/email/{email}")
    public ResponseEntity<AdminDTO> getProfileByEmail(@PathVariable String email) {
        logger.info("Requête de récupération du profil admin par email: " + email);
        AdminDTO profile = adminService.getAdminProfileByEmail(email);
        return ResponseEntity.ok(profile);
    }
    
    /**
     * Mettre à jour son profil
     */
    @PutMapping("/profile/{id}")
    public ResponseEntity<AdminDTO> updateProfile(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAdminRequest request) {
        logger.info("Requête de mise à jour du profil admin: " + id);
        AdminDTO updated = adminService.updateAdminProfile(id, request);
        return ResponseEntity.ok(updated);
    }
    
    /**
     * Désactiver un compte admin
     */
    @PatchMapping("/profile/{id}/deactivate")
    public ResponseEntity<String> deactivateAdmin(@PathVariable Long id) {
        logger.info("Requête de désactivation du compte admin: " + id);
        adminService.deactivateAdmin(id);
        return ResponseEntity.ok("Compte administrateur désactivé avec succès");
    }
    
    /**
     * Activer un compte admin
     */
    @PatchMapping("/profile/{id}/activate")
    public ResponseEntity<String> activateAdmin(@PathVariable Long id) {
        logger.info("Requête d'activation du compte admin: " + id);
        adminService.activateAdmin(id);
        return ResponseEntity.ok("Compte administrateur activé avec succès");
    }
    
    /**
     * Supprimer définitivement un compte admin
     */
    @DeleteMapping("/profile/{id}")
    public ResponseEntity<String> deleteAdmin(@PathVariable Long id) {
        logger.info("Requête de suppression définitive du compte admin: " + id);
        adminService.deleteAdmin(id);
        return ResponseEntity.ok("Compte administrateur supprimé définitivement");
    }
    
    /**
     * Télécharger une image de profil pour un admin
     */
    @PostMapping("/profile/{id}/upload-image")
    public ResponseEntity<String> uploadProfileImage(@PathVariable Long id, @RequestParam("image") MultipartFile file) {
        try {
            logger.info("Requête de téléchargement d'image de profil pour l'admin: " + id);
            String imageUrl = adminService.telechargerImageProfil(id, file);
            return ResponseEntity.ok(imageUrl);
        } catch (Exception e) {
            logger.severe("Erreur lors du téléchargement de l'image de profil pour l'admin " + id + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors du téléchargement de l'image: " + e.getMessage());
        }
    }

    //===================== GESTION DES CONTENUS =====================
    /**
     * Ajouter un contenu éducatif (audio ou vidéo)
     * Note: Les contenus ne peuvent pas être modifiés, seulement ajoutés ou supprimés
     */
    @PostMapping("/contenus")
    public ResponseEntity<ContenuDTO> ajouterContenu(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody ContenuDTO dto) {
        logger.info("Requête d'ajout de contenu");
        
        // Extraire l'ID de l'admin du token JWT
        Long adminId = jwtUtils.getUserIdFromToken(authHeader.substring(7)); // "Bearer " + token
        dto.setAdminId(adminId);
        ContenuDTO created = adminService.ajouterContenu(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    /**
     * Récupérer un contenu par son ID
     */
    @GetMapping("/contenus/{id}")
    public ResponseEntity<ContenuDTO> getContenu(@PathVariable Long id) {
        logger.info("Requête de récupération du contenu: " + id);
        ContenuDTO contenu = adminService.getContenu(id);
        return ResponseEntity.ok(contenu);
    }

    /**
     * Supprimer un contenu
     */
    @DeleteMapping("/contenus/{id}")
    public ResponseEntity<String> supprimerContenu(@PathVariable Long id) {
        logger.info("Requête de suppression du contenu: " + id);
        adminService.supprimerContenu(id);
        return ResponseEntity.ok("Contenu supprimé avec succès");
    }

    /**
     * Lister tous les contenus
     */
    @GetMapping("/contenus")
    public ResponseEntity<List<ContenuDTO>> listerContenus() {
        logger.info("Requête de liste de tous les contenus");
        List<ContenuDTO> contenus = adminService.listerContenus();
        return ResponseEntity.ok(contenus);
    }
    
    /**
     * Lister les contenus par type de catégorie (AUDIOS, VIDEOS, INSTITUTION_FINANCIERE)
     * @param type Type de catégorie
     */
    @GetMapping("/contenus/type/{type}")
    public ResponseEntity<List<ContenuDTO>> listerContenusParType(@PathVariable String type) {
        logger.info("Requête de liste des contenus par type: " + type);
        List<ContenuDTO> contenus = adminService.listerContenusParType(type);
        return ResponseEntity.ok(contenus);
    }
    
    /**
     * Lister les contenus triés par date d'ajout
     * @param ordre "asc" ou "desc" (par défaut "desc")
     */
    @GetMapping("/contenus/par-date")
    public ResponseEntity<List<ContenuDTO>> listerContenusParDate(
            @RequestParam(defaultValue = "desc") String ordre) {
        logger.info("Requête de liste des contenus par date (ordre: " + ordre + ")");
        List<ContenuDTO> contenus = adminService.listerContenusParDate(ordre);
        return ResponseEntity.ok(contenus);
    }
    
    /**
     * Lister les contenus avec durée (AUDIO et VIDEO uniquement)
     */
    @GetMapping("/contenus/avec-duree")
    public ResponseEntity<List<ContenuDTO>> listerContenusAvecDuree() {
        logger.info("Requête de liste des contenus avec durée");
        List<ContenuDTO> contenus = adminService.listerContenusAvecDuree();
        return ResponseEntity.ok(contenus);
    }
    
    /**
     * Lister les contenus par TypeInfo (SANTE, NUTRITION, DROIT, VIDEO_FORMATION)
     * @param typeInfo Type d'information recherché
     */
    @GetMapping("/contenus/type-info/{typeInfo}")
    public ResponseEntity<List<ContenuDTO>> listerContenusParTypeInfo(@PathVariable String typeInfo) {
        logger.info("Requête de liste des contenus par TypeInfo: " + typeInfo);
        List<ContenuDTO> contenus = adminService.listerContenusParTypeInfo(typeInfo);
        return ResponseEntity.ok(contenus);
    }

    //===================== GESTION DES INSTITUTIONS FINANCIÈRES =====================
    /**
     * Ajouter une institution financière
     */
    @PostMapping("/institutions")
    public ResponseEntity<InstitutionFinanciereDTO> ajouterInstitution(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody InstitutionFinanciereDTO dto) {
        logger.info("Requête d'ajout d'institution financière");
        
        // Extraire l'ID de l'admin du token JWT
        Long adminId = jwtUtils.getUserIdFromToken(authHeader.substring(7)); // "Bearer " + token
        InstitutionFinanciereDTO created = adminService.ajouterInstitution(adminId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    /**
     * Modifier une institution financière
     */
    @PutMapping("/institutions/{id}")
    public ResponseEntity<InstitutionFinanciereDTO> modifierInstitution(
            @PathVariable Long id,
            @Valid @RequestBody InstitutionFinanciereDTO dto) {
        logger.info("Requête de modification de l'institution: " + id);
        InstitutionFinanciereDTO updated = adminService.modifierInstitution(id, dto);
        return ResponseEntity.ok(updated);
    }
    
    /**
     * Récupérer une institution par son ID
     */
    @GetMapping("/institutions/{id}")
    public ResponseEntity<InstitutionFinanciereDTO> getInstitution(@PathVariable Long id) {
        logger.info("Requête de récupération de l'institution: " + id);
        InstitutionFinanciereDTO institution = adminService.getInstitution(id);
        return ResponseEntity.ok(institution);
    }

    /**
     * Supprimer une institution financière
     */
    @DeleteMapping("/institutions/{id}")
    public ResponseEntity<String> supprimerInstitution(@PathVariable Long id) {
        logger.info("Requête de suppression de l'institution: " + id);
        adminService.supprimerInstitution(id);
        return ResponseEntity.ok("Institution supprimée avec succès");
    }

    /**
     * Lister toutes les institutions financières
     */
    @GetMapping("/institutions")
    public ResponseEntity<List<InstitutionFinanciereDTO>> listerInstitutions() {
        logger.info("Requête de liste de toutes les institutions");
        List<InstitutionFinanciereDTO> institutions = adminService.listerInstitutions();
        return ResponseEntity.ok(institutions);
    }
    
    //===================== GESTION DES UTILISATEURS =====================
    
    /**
     * Lister tous les utilisateurs
     */
    @GetMapping("/utilisateurs")
    public ResponseEntity<List<UtilisateurDTO>> listerTousLesUtilisateurs() {
        logger.info("Requête de liste de tous les utilisateurs");
        List<UtilisateurDTO> utilisateurs = adminService.listerTousLesUtilisateurs();
        return ResponseEntity.ok(utilisateurs);
    }
    
    /**
     * Récupérer un utilisateur par son ID
     */
    @GetMapping("/utilisateurs/{id}")
    public ResponseEntity<UtilisateurDTO> getUtilisateur(@PathVariable Long id) {
        logger.info("Requête de récupération de l'utilisateur: " + id);
        UtilisateurDTO utilisateur = adminService.getUtilisateur(id);
        return ResponseEntity.ok(utilisateur);
    }
    
    /**
     * Lister tous les utilisateurs actifs
     */
    @GetMapping("/utilisateurs/actifs")
    public ResponseEntity<List<UtilisateurDTO>> listerUtilisateursActifs() {
        logger.info("Requête de liste des utilisateurs actifs");
        List<UtilisateurDTO> utilisateurs = adminService.listerUtilisateursActifs();
        return ResponseEntity.ok(utilisateurs);
    }
    
    /**
     * Lister tous les utilisateurs inactifs
     */
    @GetMapping("/utilisateurs/inactifs")
    public ResponseEntity<List<UtilisateurDTO>> listerUtilisateursInactifs() {
        logger.info("Requête de liste des utilisateurs inactifs");
        List<UtilisateurDTO> utilisateurs = adminService.listerUtilisateursInactifs();
        return ResponseEntity.ok(utilisateurs);
    }
    
    /**
     * Lister les utilisateurs triés par date de création
     * @param ordre "asc" ou "desc" (par défaut "desc")
     */
    @GetMapping("/utilisateurs/par-date")
    public ResponseEntity<List<UtilisateurDTO>> listerUtilisateursParDate(
            @RequestParam(defaultValue = "desc") String ordre) {
        logger.info("Requête de liste des utilisateurs par date (ordre: " + ordre + ")");
        List<UtilisateurDTO> utilisateurs = adminService.listerUtilisateursParDate(ordre);
        return ResponseEntity.ok(utilisateurs);
    }
    
    /**
     * Activer un utilisateur
     */
    @PatchMapping("/utilisateurs/{id}/activer")
    public ResponseEntity<String> activerUtilisateur(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        logger.info("Requête d'activation de l'utilisateur: " + id);
        
        // Extraire l'ID de l'admin du token JWT
        Long adminId = jwtUtils.getUserIdFromToken(authHeader.substring(7)); // "Bearer " + token
        adminService.activerUtilisateur(adminId, id);
        return ResponseEntity.ok("Utilisateur activé avec succès");
    }
    
    /**
     * Désactiver un utilisateur
     */
    @PatchMapping("/utilisateurs/{id}/desactiver")
    public ResponseEntity<String> desactiverUtilisateur(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        logger.info("Requête de désactivation de l'utilisateur: " + id);
        
        // Extraire l'ID de l'admin du token JWT
        Long adminId = jwtUtils.getUserIdFromToken(authHeader.substring(7)); // "Bearer " + token
        adminService.desactiverUtilisateur(adminId, id);
        return ResponseEntity.ok("Utilisateur désactivé avec succès");
    }
}