package com.mussodeme.MussoDeme.services;

import com.mussodeme.MussoDeme.dto.AdminDTO;
import com.mussodeme.MussoDeme.dto.ContenuDTO;
import com.mussodeme.MussoDeme.dto.InstitutionFinanciereDTO;
import com.mussodeme.MussoDeme.dto.UpdateAdminRequest;
import com.mussodeme.MussoDeme.dto.UtilisateurDTO;
import com.mussodeme.MussoDeme.entities.*;
import com.mussodeme.MussoDeme.enums.Role;
import com.mussodeme.MussoDeme.enums.TypeCategorie;
import com.mussodeme.MussoDeme.enums.TypeInfo;
import com.mussodeme.MussoDeme.exceptions.NotFoundException;
import com.mussodeme.MussoDeme.repository.AdminRepository;
import com.mussodeme.MussoDeme.repository.CategorieRepository;
import com.mussodeme.MussoDeme.repository.ContenuRepository;
import com.mussodeme.MussoDeme.repository.FemmeRuraleRepository;
import com.mussodeme.MussoDeme.repository.InstitutionFinanciereRepository;
import com.mussodeme.MussoDeme.repository.UtilisateursRepository;
import com.mussodeme.MussoDeme.enums.TypeNotif;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.nio.file.*;
import java.io.IOException;

@Service
public class AdminService {

    private static final Logger logger = Logger.getLogger(AdminService.class.getName());
    
    private static final String IMAGE_DIRECTORY = "uploads/images/";

    private final AdminRepository adminRepository;
    private final ContenuRepository contenuRepository;
    private final CategorieRepository categorieRepository;
    private final InstitutionFinanciereRepository institutionRepository;
    private final UtilisateursRepository utilisateursRepository;
    private final FemmeRuraleRepository femmeRuraleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final SMSService smsService;
    private final NotificationService notificationService;

    // Constructor for dependency injection
    public AdminService(AdminRepository adminRepository, ContenuRepository contenuRepository, 
                       CategorieRepository categorieRepository, InstitutionFinanciereRepository institutionRepository,
                       UtilisateursRepository utilisateursRepository, FemmeRuraleRepository femmeRuraleRepository,
                       ModelMapper modelMapper, PasswordEncoder passwordEncoder, SMSService smsService,
                       NotificationService notificationService) {
        this.adminRepository = adminRepository;
        this.contenuRepository = contenuRepository;
        this.categorieRepository = categorieRepository;
        this.institutionRepository = institutionRepository;
        this.utilisateursRepository = utilisateursRepository;
        this.femmeRuraleRepository = femmeRuraleRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.smsService = smsService;
        this.notificationService = notificationService;
    }

    //================= GESTION DU PROFIL ADMIN ===================
    
    /**
     * Récupérer le profil d'un admin par son ID
     */
    public AdminDTO getAdminProfile(Long adminId) {
        logger.info("Récupération du profil de l'admin: " + adminId);
        
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> {
                    logger.warning("Admin non trouvé avec l'ID: " + adminId);
                    return new NotFoundException("Administrateur non trouvé avec l'ID: " + adminId);
                });
        
        return modelMapper.map(admin, AdminDTO.class);
    }
    
    /**
     * Récupérer le profil d'un admin par son email
     */
    public AdminDTO getAdminProfileByEmail(String email) {
        logger.info("Récupération du profil de l'admin par email: " + email);
        
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.warning("Admin non trouvé avec l'email: " + email);
                    return new NotFoundException("Administrateur non trouvé avec l'email: " + email);
                });
        
        return modelMapper.map(admin, AdminDTO.class);
    }
    
    /**
     * Mettre à jour le profil d'un admin
     */
    @Transactional
    public AdminDTO updateAdminProfile(Long adminId, @Valid UpdateAdminRequest request) {
        logger.info("Mise à jour du profil de l'admin: " + adminId);
        
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> {
                    logger.warning("Admin non trouvé avec l'ID: " + adminId);
                    return new NotFoundException("Administrateur non trouvé avec l'ID: " + adminId);
                });
        
        // Mise à jour du nom si fourni
        if (request.getNom() != null && !request.getNom().isBlank()) {
            admin.setNom(request.getNom().trim());
            logger.fine("Nom mis à jour pour l'admin " + adminId);
        }
        
        // Mise à jour de l'email si fourni
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            String newEmail = request.getEmail().trim();
            
            // Vérifier si l'email existe déjà pour un autre admin
            if (!newEmail.equals(admin.getEmail()) && adminRepository.findByEmail(newEmail).isPresent()) {
                logger.warning("Tentative de mise à jour avec un email déjà utilisé: " + newEmail);
                throw new IllegalArgumentException("Cet email est déjà utilisé par un autre administrateur");
            }
            
            admin.setEmail(newEmail);
            logger.fine("Email mis à jour pour l'admin " + adminId);
        }
        
        // Mise à jour du mot de passe si fourni
        if (request.getNouveauMotDePasse() != null && !request.getNouveauMotDePasse().isBlank()) {
            // Vérifier l'ancien mot de passe
            if (request.getAncienMotDePasse() == null || request.getAncienMotDePasse().isBlank()) {
                throw new IllegalArgumentException("L'ancien mot de passe est requis pour changer le mot de passe");
            }
            
            if (!passwordEncoder.matches(request.getAncienMotDePasse(), admin.getMotDePasse())) {
                logger.warning("Échec de mise à jour du mot de passe pour l'admin " + adminId + ": ancien mot de passe incorrect");
                throw new IllegalArgumentException("L'ancien mot de passe est incorrect");
            }
            
            admin.setMotDePasse(passwordEncoder.encode(request.getNouveauMotDePasse()));
            logger.info("Mot de passe mis à jour avec succès pour l'admin " + adminId);
        }
        
        Admin updated = adminRepository.save(admin);
        logger.info("Profil de l'admin " + adminId + " mis à jour avec succès");
        
        return modelMapper.map(updated, AdminDTO.class);
    }
    
    /**
     * Désactiver un compte admin (soft delete)
     */
    @Transactional
    public void deactivateAdmin(Long adminId) {
        logger.info("Désactivation du compte admin: " + adminId);
        
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> {
                    logger.warning("Admin non trouvé avec l'ID: " + adminId);
                    return new NotFoundException("Administrateur non trouvé avec l'ID: " + adminId);
                });
        
        admin.setActive(false);
        adminRepository.save(admin);
        
        logger.info("Compte admin " + adminId + " désactivé avec succès");
    }
    
    /**
     * Activer un compte admin
     */
    @Transactional
    public void activateAdmin(Long adminId) {
        logger.info("Activation du compte admin: " + adminId);
        
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> {
                    logger.warning("Admin non trouvé avec l'ID: " + adminId);
                    return new NotFoundException("Administrateur non trouvé avec l'ID: " + adminId);
                });
        
        admin.setActive(true);
        adminRepository.save(admin);
        
        logger.info("Compte admin " + adminId + " activé avec succès");
    }
    
    /**
     * Supprimer définitivement un compte admin (hard delete)
     * Condition: L'admin ne doit gérer aucune donnée sur le système
     */
    @Transactional
    public void deleteAdmin(Long adminId) {
        logger.info("Tentative de suppression définitive du compte admin: " + adminId);
        
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> {
                    logger.warning("Admin non trouvé avec l'ID: " + adminId);
                    return new NotFoundException("Administrateur non trouvé avec l'ID: " + adminId);
                });
        
        // Vérifier que l'admin ne gère pas de contenus
        if (admin.getContenus() != null && !admin.getContenus().isEmpty()) {
            int nbContenus = admin.getContenus().size();
            logger.warning("Impossible de supprimer l'admin " + adminId + ": " + nbContenus + " contenu(s) rattaché(s)");
            throw new IllegalStateException(
                String.format("Impossible de supprimer cet administrateur. " +
                    "Il gère encore %d contenu(s). Veuillez d'abord réassigner ou supprimer ces contenus.", 
                    nbContenus)
            );
        }
        
        // Vérifier que l'admin ne gère pas de catégories
        if (admin.getCategories() != null && !admin.getCategories().isEmpty()) {
            int nbCategories = admin.getCategories().size();
            logger.warning("Impossible de supprimer l'admin " + adminId + ": " + nbCategories + " catégorie(s) rattachée(s)");
            throw new IllegalStateException(
                String.format("Impossible de supprimer cet administrateur. " +
                    "Il gère encore %d catégorie(s). Veuillez d'abord réassigner ou supprimer ces catégories.", 
                    nbCategories)
            );
        }
        
        // Vérifier que l'admin n'a pas d'actions de gestion en cours
        if (admin.getGestionsAdmin() != null && !admin.getGestionsAdmin().isEmpty()) {
            int nbGestions = admin.getGestionsAdmin().size();
            logger.warning("Impossible de supprimer l'admin " + adminId + ": " + nbGestions + " action(s) de gestion rattachée(s)");
            throw new IllegalStateException(
                String.format("Impossible de supprimer cet administrateur. " +
                    "Il a encore %d action(s) de gestion associée(s). Veuillez d'abord nettoyer ces données.", 
                    nbGestions)
            );
        }
        
        // Si toutes les vérifications passent, supprimer le compte
        adminRepository.deleteById(adminId);
        logger.info("Compte admin " + adminId + " supprimé définitivement (Nom: " + admin.getNom() + ", Email: " + admin.getEmail() + ")");
    }
    
    /**
     * Télécharger une image de profil pour un admin
     */
    public String telechargerImageProfil(Long adminId, MultipartFile imageFile) {
        if (imageFile.isEmpty()) {
            throw new IllegalArgumentException("Aucun fichier image reçu pour l'administrateur " + adminId);
        }

        try {
            // Créer le répertoire s'il n'existe pas
            Path imageDir = Paths.get(IMAGE_DIRECTORY);
            if (!Files.exists(imageDir)) {
                Files.createDirectories(imageDir);
            }
            
            String fileName = "profile_" + adminId + "_" + System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Path filePath = Paths.get(IMAGE_DIRECTORY + fileName);
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Mettre à jour l'URL de l'image dans la base de données
            Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> {
                    logger.warning("Admin non trouvé avec l'ID: " + adminId);
                    return new NotFoundException("Administrateur non trouvé avec l'ID: " + adminId);
                });
            
            String imageUrl = "/uploads/images/" + fileName;
            admin.setImageUrl(imageUrl);
            adminRepository.save(admin);

            return imageUrl;
        } catch (IOException e) {
            logger.severe("Erreur lors du téléchargement de l'image pour l'admin " + adminId + ": " + e.getMessage());
            throw new RuntimeException("Erreur lors du téléchargement de l'image : " + e.getMessage());
        }
    }


    //======================== GESTION DES CONTENUS =========================
    
    /**
     * Ajouter un contenu éducatif (audio ou vidéo)
     * Note: Les contenus ne peuvent pas être modifiés, seulement ajoutés ou supprimés
     */
    @Transactional
    public ContenuDTO ajouterContenu(@Valid ContenuDTO dto) {
        logger.info("Ajout d'un nouveau contenu par l'admin: " + dto.getAdminId());
        
        // Validation des données
        if (dto.getTitre() == null || dto.getTitre().isBlank()) {
            throw new IllegalArgumentException("Le titre du contenu est obligatoire");
        }
        
        Admin admin = adminRepository.findById(dto.getAdminId())
                .orElseThrow(() -> {
                    logger.warning("Admin non trouvé avec l'ID: " + dto.getAdminId());
                    return new NotFoundException("Administrateur non trouvé avec l'ID: " + dto.getAdminId());
                });

        // Convertir la chaîne en énumération TypeCategorie
        TypeCategorie typeCategorie = TypeCategorie.valueOf(dto.getTypeCategorie());

        Contenu contenu = new Contenu();
        contenu.setTitre(dto.getTitre().trim());
        contenu.setDescription(dto.getDescription() != null ? dto.getDescription().trim() : null);
        contenu.setUrlContenu(dto.getUrlContenu());
        contenu.setDuree(dto.getDuree());
        contenu.setTypeInfo(dto.getTypeInfo());
        contenu.setCategorie(typeCategorie);
        contenu.setAdmin(admin);

        Contenu saved = contenuRepository.save(contenu);
        logger.info("Contenu créé avec succès - ID: " + saved.getId() + ", Titre: " + saved.getTitre());
        
        // Envoyer SMS de notification aux femmes rurales
        List<FemmeRurale> femmes = femmeRuraleRepository.findAll();
        for (FemmeRurale femme : femmes) {
            smsService.envoyerSMSNouveauContenu(saved, femme, admin.getNom());
        }
        
        dto.setId(saved.getId());
        return dto;
    }
    
    /**
     * Supprimer un contenu
     */
    @Transactional
    public void supprimerContenu(Long id) {
        logger.info("Suppression du contenu: " + id);
        
        if (!contenuRepository.existsById(id)) {
            logger.warning("Contenu non trouvé avec l'ID: " + id);
            throw new NotFoundException("Contenu non trouvé avec l'ID: " + id);
        }
        
        contenuRepository.deleteById(id);
        logger.info("Contenu " + id + " supprimé avec succès");
    }

    /**
     * Récupérer un contenu par son ID
     */
    public ContenuDTO getContenu(Long id) {
        logger.info("Récupération du contenu: " + id);
        
        Contenu contenu = contenuRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warning("Contenu non trouvé avec l'ID: " + id);
                    return new NotFoundException("Contenu non trouvé avec l'ID: " + id);
                });
        
        return modelMapper.map(contenu, ContenuDTO.class);
    }
    
    /**
     * Lister tous les contenus
     */
    public List<ContenuDTO> listerContenus() {
        logger.info("Récupération de la liste de tous les contenus");
        
        List<Contenu> contenus = contenuRepository.findAll();
        logger.fine(contenus.size() + " contenus trouvés");
        
        return contenus.stream()
                .map(c -> new ContenuDTO(
                        c.getId(),
                        c.getTitre(),
                        c.getDescription(),
                        c.getUrlContenu(),
                        c.getDuree(),
                        c.getAdmin().getId(),
                        c.getCategorie().name()
                ))
                .collect(Collectors.toList());
    }
    
    /**
     * Lister les contenus par type de catégorie (AUDIOS, VIDEOS, INSTITUTION_FINANCIERE)
     */
    public List<ContenuDTO> listerContenusParType(String typeCategorie) {
        logger.info("Récupération des contenus par type: " + typeCategorie);
        
        try {
            TypeCategorie type = TypeCategorie.valueOf(typeCategorie.toUpperCase());
            List<Contenu> contenus = contenuRepository.findByCategorieType(type);
            logger.fine(contenus.size() + " contenus de type " + type + " trouvés");
            
            return contenus.stream()
                    .map(c -> new ContenuDTO(
                            c.getId(),
                            c.getTitre(),
                            c.getDescription(),
                            c.getUrlContenu(),
                            c.getDuree(),
                            c.getAdmin().getId(),
                            c.getCategorie().name()
                    ))
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            logger.warning("Type de catégorie invalide: " + typeCategorie);
            throw new IllegalArgumentException("Type de catégorie invalide. Valeurs acceptées: AUDIOS, VIDEOS, INSTITUTION_FINANCIERE");
        }
    }
    
    /**
     * Lister les contenus triés par date d'ajout
     * @param ordre "asc" pour ascendant (plus ancien d'abord), "desc" pour descendant (plus récent d'abord)
     */
    public List<ContenuDTO> listerContenusParDate(String ordre) {
        logger.info("Récupération des contenus triés par date (ordre: " + ordre + ")");
        
        List<Contenu> contenus;
        if ("asc".equalsIgnoreCase(ordre)) {
            contenus = contenuRepository.findAllOrderByDateAsc();
        } else {
            contenus = contenuRepository.findAllOrderByDateDesc();
        }
        
        logger.fine(contenus.size() + " contenus trouvés");
        
        return contenus.stream()
                .map(c -> new ContenuDTO(
                        c.getId(),
                        c.getTitre(),
                        c.getDescription(),
                        c.getUrlContenu(),
                        c.getDuree(),
                        c.getAdmin().getId(),
                        c.getCategorie().name()
                ))
                .collect(Collectors.toList());
    }
    
    /**
     * Lister les contenus avec durée (AUDIO et VIDEO uniquement)
     */
    public List<ContenuDTO> listerContenusAvecDuree() {
        logger.info("Récupération des contenus avec durée (AUDIO et VIDEO)");
        
        List<Contenu> contenus = contenuRepository.findAllWithDuration();
        logger.fine(contenus.size() + " contenus avec durée trouvés");
        
        return contenus.stream()
                .map(c -> new ContenuDTO(
                        c.getId(),
                        c.getTitre(),
                        c.getDescription(),
                        c.getUrlContenu(),
                        c.getDuree(),
                        c.getAdmin().getId(),
                        c.getCategorie().name()
                ))
                .collect(Collectors.toList());
    }
    
    /**
     * Lister les contenus par TypeInfo (SANTE, NUTRITION, DROIT, VIDEO_FORMATION)
     * Utile pour les femmes rurales qui cherchent par type d'information
     */
    public List<ContenuDTO> listerContenusParTypeInfo(String typeInfo) {
        logger.info("Récupération des contenus par TypeInfo: " + typeInfo);
        
        try {
            TypeInfo type = TypeInfo.valueOf(typeInfo.trim().toUpperCase());
            List<Contenu> contenus = contenuRepository.findByTypeInfo(type);
            logger.fine(contenus.size() + " contenus de TypeInfo " + type + " trouvés");
            
            return contenus.stream()
                    .map(c -> modelMapper.map(c, ContenuDTO.class))
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            logger.warning("TypeInfo invalide: " + typeInfo);
            throw new IllegalArgumentException("TypeInfo invalide. Valeurs acceptées: SANTE, NUTRITION, DROIT, VIDEO_FORMATION");
        }
    }

    //====================== GESTION DES INSTITUTIONS FINANCIÈRES ============================
    
    /**
     * Ajouter une institution financière
     */
    @Transactional
    public InstitutionFinanciereDTO ajouterInstitution(Long adminId, @Valid InstitutionFinanciereDTO dto) {
        logger.info("Ajout d'une nouvelle institution financière: " + dto.getNom());
        
        // Validation
        if (dto.getNom() == null || dto.getNom().isBlank()) {
            throw new IllegalArgumentException("Le nom de l'institution est obligatoire");
        }
        
        // Vérifier que l'admin existe
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> {
                    logger.warning("Admin non trouvé avec l'ID: " + adminId);
                    return new NotFoundException("Administrateur non trouvé avec l'ID: " + adminId);
                });
        
        InstitutionFinanciere institution = new InstitutionFinanciere();
        institution.setNom(dto.getNom().trim());
        institution.setNumeroTel(dto.getNumeroTel() != null ? dto.getNumeroTel().trim() : null);
        institution.setDescription(dto.getDescription() != null ? dto.getDescription().trim() : null);
        institution.setLogoUrl(dto.getLogoUrl());

        InstitutionFinanciere saved = institutionRepository.save(institution);
        logger.info("Institution financière créée avec succès - ID: " + saved.getId() + ", Nom: " + saved.getNom());
        
        // Envoyer SMS de notification aux femmes rurales
        List<FemmeRurale> femmes = femmeRuraleRepository.findAll();
        for (FemmeRurale femme : femmes) {
            smsService.envoyerSMSNouvelleInstitution(saved, femme, admin.getNom());
        }
        
        dto.setId(saved.getId());
        return dto;
    }

    /**
     * Modifier une institution financière
     */
    @Transactional
    public InstitutionFinanciereDTO modifierInstitution(Long institutionId, @Valid InstitutionFinanciereDTO dto) {
        logger.info("Modification de l'institution financière: " + institutionId);
        
        InstitutionFinanciere institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> {
                    logger.warning("Institution non trouvée avec l'ID: " + institutionId);
                    return new NotFoundException("Institution financière non trouvée avec l'ID: " + institutionId);
                });
        
        // Mise à jour des champs si fournis
        if (dto.getNom() != null && !dto.getNom().isBlank()) {
            institution.setNom(dto.getNom().trim());
        }
        
        if (dto.getNumeroTel() != null) {
            institution.setNumeroTel(dto.getNumeroTel().trim());
        }
        
        if (dto.getDescription() != null) {
            institution.setDescription(dto.getDescription().trim());
        }
        
        if (dto.getLogoUrl() != null && !dto.getLogoUrl().isBlank()) {
            institution.setLogoUrl(dto.getLogoUrl());
        }
        
        InstitutionFinanciere updated = institutionRepository.save(institution);
        logger.info("Institution financière " + institutionId + " modifiée avec succès");
        
        return modelMapper.map(updated, InstitutionFinanciereDTO.class);
    }
    
    /**
     * Supprimer une institution financière
     */
    @Transactional
    public void supprimerInstitution(Long id) {
        logger.info("Suppression de l'institution financière: " + id);
        
        if (!institutionRepository.existsById(id)) {
            logger.warning("Institution non trouvée avec l'ID: " + id);
            throw new NotFoundException("Institution financière non trouvée avec l'ID: " + id);
        }
        
        institutionRepository.deleteById(id);
        logger.info("Institution financière " + id + " supprimée avec succès");
    }

    /**
     * Récupérer une institution par son ID
     */
    public InstitutionFinanciereDTO getInstitution(Long id) {
        logger.info("Récupération de l'institution financière: " + id);
        
        InstitutionFinanciere institution = institutionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warning("Institution non trouvée avec l'ID: " + id);
                    return new NotFoundException("Institution financière non trouvée avec l'ID: " + id);
                });
        
        return modelMapper.map(institution, InstitutionFinanciereDTO.class);
    }
    
    /**
     * Lister toutes les institutions financières
     */
    public List<InstitutionFinanciereDTO> listerInstitutions() {
        logger.info("Récupération de la liste de toutes les institutions financières");
        
        List<InstitutionFinanciere> institutions = institutionRepository.findAll();
        logger.fine(institutions.size() + " institutions trouvées");
        
        return institutions.stream()
                .map(i -> new InstitutionFinanciereDTO(
                        i.getId(),
                        i.getNom(),
                        i.getNumeroTel(),
                        i.getDescription(),
                        i.getLogoUrl()
                ))
                .collect(Collectors.toList());
    }
    
    //====================== GESTION DES UTILISATEURS ============================
    
    /**
     * Lister tous les utilisateurs (toutes catégories confondues)
     */
    public List<UtilisateurDTO> listerTousLesUtilisateurs() {
        logger.info("Récupération de la liste de tous les utilisateurs");
        
        List<Utilisateur> utilisateurs = utilisateursRepository.findAll();
        logger.fine(utilisateurs.size() + " utilisateurs trouvés au total");
        
        return utilisateurs.stream()
                .map(u -> modelMapper.map(u, UtilisateurDTO.class))
                .collect(Collectors.toList());
    }
    
    /**
     * Récupérer un utilisateur par son ID
     */
    public UtilisateurDTO getUtilisateur(Long id) {
        logger.info("Récupération de l'utilisateur: " + id);
        
        Utilisateur utilisateur = utilisateursRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warning("Utilisateur non trouvé avec l'ID: " + id);
                    return new NotFoundException("Utilisateur non trouvé avec l'ID: " + id);
                });
        
        return modelMapper.map(utilisateur, UtilisateurDTO.class);
    }
    
    /**
     * Lister tous les utilisateurs actifs
     */
    public List<UtilisateurDTO> listerUtilisateursActifs() {
        logger.info("Récupération de la liste des utilisateurs actifs");
        
        List<Utilisateur> utilisateurs = utilisateursRepository.findByActiveTrue();
        logger.fine(utilisateurs.size() + " utilisateurs actifs trouvés");
        
        return utilisateurs.stream()
                .map(u -> modelMapper.map(u, UtilisateurDTO.class))
                .collect(Collectors.toList());
    }
    
    /**
     * Lister tous les utilisateurs inactifs
     */
    public List<UtilisateurDTO> listerUtilisateursInactifs() {
        logger.info("Récupération de la liste des utilisateurs inactifs");
        
        List<Utilisateur> utilisateurs = utilisateursRepository.findByActiveFalse();
        logger.fine(utilisateurs.size() + " utilisateurs inactifs trouvés");
        
        return utilisateurs.stream()
                .map(u -> modelMapper.map(u, UtilisateurDTO.class))
                .collect(Collectors.toList());
    }
    
    /**
     * Lister les utilisateurs triés par date de création
     * @param ordre "asc" pour ascendant (plus ancien d'abord), "desc" pour descendant (plus récent d'abord)
     */
    public List<UtilisateurDTO> listerUtilisateursParDate(String ordre) {
        logger.info("Récupération des utilisateurs triés par date de création (" + ordre + ")");
        
        List<Utilisateur> utilisateurs;
        
        if ("asc".equalsIgnoreCase(ordre)) {
            utilisateurs = utilisateursRepository.findAllByOrderByCreatedAtAsc();
            logger.fine("Tri ascendant: du plus ancien au plus récent");
        } else {
            utilisateurs = utilisateursRepository.findAllByOrderByCreatedAtDesc();
            logger.fine("Tri descendant: du plus récent au plus ancien");
        }
        
        logger.fine(utilisateurs.size() + " utilisateurs trouvés");
        
        return utilisateurs.stream()
                .map(u -> modelMapper.map(u, UtilisateurDTO.class))
                .collect(Collectors.toList());
    }
    
    /**
     * Helper method to create a Utilisateur object from an Admin
     * This is needed for notification services that require a Utilisateur parameter
     */
    private Utilisateur createUtilisateurFromAdmin(Admin admin) {
        return new Utilisateur() {
            @Override
            public Long getId() {
                return admin.getId();
            }
            
            @Override
            public String getNom() {
                return admin.getNom();
            }
            
            @Override
            public String getEmail() {
                return admin.getEmail();
            }
            
            @Override
            public Role getRole() {
                return admin.getRole();
            }
            
            @Override
            public boolean isActive() {
                return admin.isActive();
            }
            
            // Implement other abstract methods with default behavior
            @Override
            public String getPrenom() {
                return ""; // Admin doesn't have prenom field
            }
            
            @Override
            public String getNumeroTel() {
                return ""; // Admin doesn't have numeroTel field
            }
            
            @Override
            public String getMotCle() {
                return ""; // Admin doesn't have motCle field
            }
            
            @Override
            public String getLocalite() {
                return ""; // Admin doesn't have localite field
            }
            
            @Override
            public LocalDateTime getCreatedAt() {
                return LocalDateTime.now(); // Return current time for admin
            }
        };
    }
    
    /**
     * Activer un utilisateur
     */
    @Transactional
    public void activerUtilisateur(Long adminId, Long userId) {
        logger.info("Activation de l'utilisateur: " + userId + " par admin: " + adminId);
        
        // Vérifier que l'admin existe
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> {
                    logger.warning("Admin non trouvé avec l'ID: " + adminId);
                    return new NotFoundException("Administrateur non trouvé avec l'ID: " + adminId);
                });
        
        Utilisateur utilisateur = utilisateursRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warning("Utilisateur non trouvé avec l'ID: " + userId);
                    return new NotFoundException("Utilisateur non trouvé avec l'ID: " + userId);
                });
        
        if (utilisateur.isActive()) {
            logger.info("L'utilisateur " + userId + " est déjà actif");
            return;
        }
        
        utilisateur.setActive(true);
        utilisateursRepository.save(utilisateur);
        
        // Envoyer SMS de notification
        smsService.envoyerSMSActivationUtilisateur(utilisateur, true, admin.getNom());
        
        // Envoyer notification in-app uniquement à l'utilisateur activé
        String userMessage = String.format("Votre compte a été activé par l'administrateur %s", admin.getNom());
        notificationService.creerNotification(utilisateur, TypeNotif.INFO, userMessage);
        
        // Envoyer notification in-app à l'administrateur
        String adminMessage = String.format("Vous avez activé le compte de l'utilisateur %s %s", 
                                          utilisateur.getPrenom(), utilisateur.getNom());
        notificationService.creerNotificationAdmin(admin, TypeNotif.INFO, adminMessage);
        
        logger.info("Utilisateur " + userId + " activé avec succès (Nom: " + utilisateur.getPrenom() + " " + utilisateur.getNom() + ", Rôle: " + utilisateur.getRole() + ")");
    }
    
    /** 
     * Désactiver un utilisateur
     */
    @Transactional
    public void desactiverUtilisateur(Long adminId, Long userId) {
        logger.info("Désactivation de l'utilisateur: " + userId + " par admin: " + adminId);
        
        // Vérifier que l'admin existe
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> {
                    logger.warning("Admin non trouvé avec l'ID: " + adminId);
                    return new NotFoundException("Administrateur non trouvé avec l'ID: " + adminId);
                });
        
        Utilisateur utilisateur = utilisateursRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warning("Utilisateur non trouvé avec l'ID: " + userId);
                    return new NotFoundException("Utilisateur non trouvé avec l'ID: " + userId);
                });
        
        if (!utilisateur.isActive()) {
            logger.info("L'utilisateur " + userId + " est déjà inactif");
            return;
        }
        
        utilisateur.setActive(false);
        utilisateursRepository.save(utilisateur);
        
        // Envoyer SMS de notification
        smsService.envoyerSMSActivationUtilisateur(utilisateur, false, admin.getNom());
        
        // Envoyer notification in-app uniquement à l'utilisateur désactivé
        String userMessage = String.format("Votre compte a été désactivé par l'administrateur %s", admin.getNom());
        notificationService.creerNotification(utilisateur, TypeNotif.INFO, userMessage);
        
        // Envoyer notification in-app à l'administrateur
        String adminMessage = String.format("Vous avez désactivé le compte de l'utilisateur %s %s", 
                                          utilisateur.getPrenom(), utilisateur.getNom());
        notificationService.creerNotificationAdmin(admin, TypeNotif.INFO, adminMessage);
        
        logger.info("Utilisateur " + userId + " désactivé avec succès (Nom: " + utilisateur.getPrenom() + " " + utilisateur.getNom() + ", Rôle: " + utilisateur.getRole() + ")");
    }
}