package com.mussodeme.MussoDeme.services;

import com.mussodeme.MussoDeme.dto.*;
import com.mussodeme.MussoDeme.entities.*;
import com.mussodeme.MussoDeme.enums.ModePaiement;
import com.mussodeme.MussoDeme.enums.StatutCommande;
import com.mussodeme.MussoDeme.enums.TypeInfo;
import com.mussodeme.MussoDeme.enums.TypeProduit;
import com.mussodeme.MussoDeme.exceptions.NotFoundException;
import com.mussodeme.MussoDeme.repository.*;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.logging.Logger;

/**
 * Service pour la gestion des fonctionnalités spécifiques aux femmes rurales
 *
 * Fonctionnalités principales:
 * - Contenus audio/vidéo (santé, nutrition, droits, formations)
 * - Vente de produits artisanaux
 * - Communication vocale (appels, messages)
 * - Commandes et paiements mobile money
 * - Recherche par TypeInfo
 */
@Service
public class FemmeRuraleService {

    private static final Logger logger = Logger.getLogger(FemmeRuraleService.class.getName());

    private final FemmeRuraleRepository femmeRuraleRepository;
    private final ProduitRepository produitRepository;
    private final ContenuRepository contenuRepository;
    private final UtilisateurAudioRepository utilisateurAudioRepository;
    private final ChatVocalRepository chatVocalRepository;
    private final CoperativeRepository cooperativeRepository;
    private final AppartenanceRepository appartenanceRepository;
    private final CommandeRepository commandeRepository;
    private final PaiementRepository paiementRepository;
    private final PartageCooperativeRepository partageCooperativeRepository;
    private final HistoriqueService historiqueService;
    private final NotificationService notificationService;
    private final ModelMapper modelMapper;
    private final RechercherParLocalisationRepository rechercherParLocalisationRepository;
    private final AcheteurRepository acheteurRepository;

    // Constructeur pour l'injection de dépendances
    public FemmeRuraleService(
            FemmeRuraleRepository femmeRuraleRepository,
            ProduitRepository produitRepository,
            ContenuRepository contenuRepository,
            UtilisateurAudioRepository utilisateurAudioRepository,
            ChatVocalRepository chatVocalRepository,
            CoperativeRepository cooperativeRepository,
            AppartenanceRepository appartenanceRepository,
            CommandeRepository commandeRepository,
            PaiementRepository paiementRepository,
            PartageCooperativeRepository partageCooperativeRepository,
            HistoriqueService historiqueService,
            NotificationService notificationService,
            ModelMapper modelMapper,
            RechercherParLocalisationRepository rechercherParLocalisationRepository,
            AcheteurRepository acheteurRepository) {
        this.femmeRuraleRepository = femmeRuraleRepository;
        this.produitRepository = produitRepository;
        this.contenuRepository = contenuRepository;
        this.utilisateurAudioRepository = utilisateurAudioRepository;
        this.chatVocalRepository = chatVocalRepository;
        this.cooperativeRepository = cooperativeRepository;
        this.appartenanceRepository = appartenanceRepository;
        this.commandeRepository = commandeRepository;
        this.paiementRepository = paiementRepository;
        this.partageCooperativeRepository = partageCooperativeRepository;
        this.historiqueService = historiqueService;
        this.notificationService = notificationService;
        this.modelMapper = modelMapper;
        this.rechercherParLocalisationRepository = rechercherParLocalisationRepository;
        this.acheteurRepository = acheteurRepository;
    }

    //================== GESTION DES CONTENUS (AUDIO/VIDEO) ==================

    /**
     * Télécharger et lire un contenu audio ou vidéo
     * Enregistre la date de lecture pour traçabilité
     */
    @Transactional
    public ContenuDTO ecouterContenu(Long femmeId, Long contenuId) {
        logger.info("Femme " + femmeId + " écoute le contenu " + contenuId);

        FemmeRurale femme = femmeRuraleRepository.findById(femmeId)
                .orElseThrow(() -> new NotFoundException("Femme rurale non trouvée avec l'ID: " + femmeId));

        Contenu contenu = contenuRepository.findById(contenuId)
                .orElseThrow(() -> new NotFoundException("Contenu non trouvé avec l'ID: " + contenuId));

        // Enregistrer la lecture
        UtilisateurAudio utilisateurAudio = new UtilisateurAudio();
        utilisateurAudio.setUtilisateur(femme);
        utilisateurAudio.setContenu(contenu);
        utilisateurAudio.setDateLecture(LocalDateTime.now());

        utilisateurAudioRepository.save(utilisateurAudio);

        // Enregistrer dans l'historique
        historiqueService.enregistrerTelechargement(femme, contenu);

        logger.info("Lecture du contenu '" + contenu.getTitre() + "' enregistrée pour la femme " + femmeId);

        return modelMapper.map(contenu, ContenuDTO.class);
    }

    /**
     * Lister tous les audios disponibles
     */
    public List<ContenuDTO> listerAudios() {
        logger.info("Récupération de tous les contenus audio");
        return contenuRepository.findByTypeInfo(TypeInfo.SANTE).stream()
                .map(c -> modelMapper.map(c, ContenuDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Lister tous les vidéos disponibles
     */
    public List<ContenuDTO> listerVideos() {
        logger.info("Récupération de tous les contenus vidéo");
        return contenuRepository.findByTypeInfo(TypeInfo.VIDEO_FORMATION).stream()
                .map(c -> modelMapper.map(c, ContenuDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Lister les contenus par TypeInfo (SANTE, NUTRITION, DROIT, VIDEO_FORMATION)
     */
    public List<ContenuDTO> listerContenusParTypeInfo(String typeInfo) {
        logger.info("Récupération des contenus par TypeInfo: " + typeInfo);

        try {
            TypeInfo type = TypeInfo.valueOf(typeInfo.toUpperCase());
            List<Contenu> contenus = contenuRepository.findByTypeInfo(type);

            return contenus.stream()
                    .map(c -> modelMapper.map(c, ContenuDTO.class))
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            logger.warning("TypeInfo invalide: " + typeInfo);
            throw new IllegalArgumentException("TypeInfo invalide. Valeurs acceptées: SANTE, NUTRITION, DROIT, VIDEO_FORMATION");
        }
    }

    /**
     * Récupérer l'historique de lecture d'une femme
     */
    public List<UtilisateurAudioDTO> getHistoriqueLecture(Long femmeId) {
        logger.info("Récupération de l'historique de lecture pour la femme " + femmeId);

        FemmeRurale femme = femmeRuraleRepository.findById(femmeId)
                .orElseThrow(() -> new NotFoundException("Femme rurale non trouvée avec l'ID: " + femmeId));

        List<UtilisateurAudio> historique = utilisateurAudioRepository.findByUtilisateur(femme);

        return historique.stream()
                .map(ua -> modelMapper.map(ua, UtilisateurAudioDTO.class))
                .collect(Collectors.toList());
    }

    //================== GESTION DES PRODUITS ==================

    /**
     * Sauvegarder physiquement une image de produit sur le serveur
     * et retourner le nom de fichier stocké (à mettre dans Produit.image).
     */
    @Transactional
    public String sauvegarderImageProduit(Long femmeId, MultipartFile imageFile) throws IOException {
        logger.info("Femme " + femmeId + " upload une image de produit : "
                + (imageFile != null ? imageFile.getOriginalFilename() : "null"));

        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("Le fichier image est vide");
        }

        // Vérifier que la femme existe (pour cohérence métier)
        FemmeRurale femme = femmeRuraleRepository.findById(femmeId)
                .orElseThrow(() -> new NotFoundException("Femme rurale non trouvée avec l'ID: " + femmeId));

        String originalFilename = StringUtils.cleanPath(imageFile.getOriginalFilename());
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex >= 0) {
            extension = originalFilename.substring(dotIndex); // .jpg, .png, ...
        }

        // Nom unique basé sur l'ID de la femme + timestamp
        String baseName = "prod_" + femme.getId() + "_" + System.currentTimeMillis();
        String fileName = baseName + extension;

        // Dossier "uploads" à la racine du projet
        Path uploadsDir = Paths.get("uploads");
        if (!Files.exists(uploadsDir)) {
            Files.createDirectories(uploadsDir);
        }

        Path target = uploadsDir.resolve(fileName);
        Files.copy(imageFile.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        logger.info("Image de produit sauvegardée sur le serveur : " + fileName);

        // On retourne seulement le nom du fichier ; côté Flutter on construira l’URL
        return fileName;
    }

    /**
     * Publier un produit avec audio de guide
     */
    @Transactional
    public ProduitDTO publierProduit(Long femmeId, @Valid ProduitDTO produitDTO) {
        logger.info("Femme " + femmeId + " publie un nouveau produit: " + produitDTO.getNom());

        FemmeRurale femme = femmeRuraleRepository.findById(femmeId)
                .orElseThrow(() -> new NotFoundException("Femme rurale non trouvée avec l'ID: " + femmeId));

        Produit produit = new Produit();
        produit.setNom(produitDTO.getNom());
        produit.setDescription(produitDTO.getDescription());
        produit.setImage(produitDTO.getImage());
        produit.setQuantite(produitDTO.getQuantite());
        produit.setPrix(produitDTO.getPrix());
        produit.setUnite(produitDTO.getUnite());        // ✅ ici
        produit.setTypeProduit(produitDTO.getTypeProduit());
        produit.setAudioGuideUrl(produitDTO.getAudioGuideUrl());
        produit.setFemmeRurale(femme);

        Produit saved = produitRepository.save(produit);
        historiqueService.enregistrerPublication(femme, saved);

        return modelMapper.map(saved, ProduitDTO.class);
    }


    /**
     * Modifier un produit
     */
    @Transactional
    public ProduitDTO modifierProduit(Long femmeId, Long produitId, @Valid ProduitDTO produitDTO) {
        logger.info("Femme " + femmeId + " modifie le produit " + produitId);

        FemmeRurale femme = femmeRuraleRepository.findById(femmeId)
                .orElseThrow(() -> new NotFoundException("Femme rurale non trouvée avec l'ID: " + femmeId));

        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new NotFoundException("Produit non trouvé avec l'ID: " + produitId));

        // Vérifier que le produit appartient à cette femme
        if (!produit.getFemmeRurale().getId().equals(femmeId)) {
            throw new IllegalArgumentException("Vous ne pouvez modifier que vos propres produits");
        }

        StringBuilder details = new StringBuilder();

        // Mise à jour des champs
        if (produitDTO.getNom() != null && !produitDTO.getNom().equals(produit.getNom())) {
            details.append(String.format("Nom: %s → %s; ", produit.getNom(), produitDTO.getNom()));
            produit.setNom(produitDTO.getNom());
        }

        if (produitDTO.getDescription() != null) {
            produit.setDescription(produitDTO.getDescription());
        }

        if (produitDTO.getImage() != null) {
            produit.setImage(produitDTO.getImage());
        }

        if (produitDTO.getQuantite() != null && !produitDTO.getQuantite().equals(produit.getQuantite())) {
            details.append(String.format("Quantité: %d → %d; ", produit.getQuantite(), produitDTO.getQuantite()));
            produit.setQuantite(produitDTO.getQuantite());
        }

        if (produitDTO.getPrix() != null && !produitDTO.getPrix().equals(produit.getPrix())) {
            details.append(String.format("Prix: %,.0f → %,.0f FCFA; ", produit.getPrix(), produitDTO.getPrix()));
            produit.setPrix(produitDTO.getPrix());
        }

        if (produitDTO.getTypeProduit() != null) {
            produit.setTypeProduit(produitDTO.getTypeProduit());
        }

        if (produitDTO.getAudioGuideUrl() != null) {
            produit.setAudioGuideUrl(produitDTO.getAudioGuideUrl());
        }

        if (produitDTO.getUnite() != null && !produitDTO.getUnite().equals(produit.getUnite())) {
            details.append(String.format("Unité: %s → %s; ", produit.getUnite(), produitDTO.getUnite()));
            produit.setUnite(produitDTO.getUnite());         // ✅
        }

        Produit updated = produitRepository.save(produit);

        // Enregistrer dans l'historique
        historiqueService.enregistrerModification(femme, updated, details.toString());

        logger.info("Produit " + produitId + " modifié avec succès par la femme " + femmeId);

        return modelMapper.map(updated, ProduitDTO.class);
    }

    /**
     * Supprimer un produit
     */
    @Transactional
    public void supprimerProduit(Long femmeId, Long produitId) {
        logger.info("Femme " + femmeId + " supprime le produit " + produitId);

        FemmeRurale femme = femmeRuraleRepository.findById(femmeId)
                .orElseThrow(() -> new NotFoundException("Femme rurale non trouvée avec l'ID: " + femmeId));

        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new NotFoundException("Produit non trouvé avec l'ID: " + produitId));

        // Vérifier que le produit appartient à cette femme
        if (!produit.getFemmeRurale().getId().equals(femmeId)) {
            throw new IllegalArgumentException("Vous ne pouvez supprimer que vos propres produits");
        }

        produitRepository.delete(produit);

        // Enregistrer dans l'historique
        historiqueService.enregistrerSuppressionProduit(femme, produit);

        logger.info("Produit " + produitId + " supprimé avec succès");
    }

    /**
     * Voir ses propres produits
     */
    public List<ProduitDTO> voirMesProduits(Long femmeId) {
        logger.info("Récupération des produits de la femme " + femmeId);

        List<Produit> produits = produitRepository.findByFemmeRuraleId(femmeId);

        return produits.stream()
                .map(p -> modelMapper.map(p, ProduitDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Commandes où la femme est ACHETEUSE
     */
    public List<CommandeDTO> getCommandesCommeAcheteur(Long femmeId) {
        logger.info("Récupération des commandes (acheteur) pour la femme " + femmeId);

        List<Commande> commandes = commandeRepository.findByAcheteurId(femmeId);

        return commandes.stream()
                .map(c -> modelMapper.map(c, CommandeDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Commandes où la femme est VENDEUSE (ses ventes)
     */
    public List<CommandeDTO> getCommandesCommeVendeur(Long femmeId) {
        logger.info("Récupération des commandes (vendeur) pour la femme " + femmeId);

        List<Commande> commandes = commandeRepository.findByProduit_FemmeRurale_Id(femmeId);

        return commandes.stream()
                .map(c -> modelMapper.map(c, CommandeDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Voir mes commandes (version acheteuse uniquement)
     */
    public List<CommandeDTO> voirMesCommandes(Long femmeId) {
        return getCommandesCommeAcheteur(femmeId);
    }


    /**
     * Voir tous les produits (de toutes les femmes)
     */
    public List<ProduitDTO> voirTousLesProduits() {
        logger.info("Récupération de tous les produits disponibles");

        List<Produit> produits = produitRepository.findAll();

        return produits.stream()
                .map(p -> modelMapper.map(p, ProduitDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Récupérer un produit avec son audio de guide
     */
    public ProduitDTO getProduitAvecAudioGuide(Long produitId) {
        logger.info("Récupération du produit " + produitId + " avec audio de guide");

        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new NotFoundException("Produit non trouvé avec l'ID: " + produitId));

        return modelMapper.map(produit, ProduitDTO.class);
    }

    /**
     * Rechercher des produits par type (vocal)
     */
    public List<ProduitDTO> rechercherProduitsParType(TypeProduit typeProduit) {
        logger.info("Recherche vocale de produits de type: " + typeProduit);

        List<Produit> produits = produitRepository.findByTypeProduit(typeProduit);

        return produits.stream()
                .map(p -> modelMapper.map(p, ProduitDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Rechercher des produits disponibles par type (vocal)
     */
    public List<ProduitDTO> rechercherProduitsDisponiblesParType(TypeProduit typeProduit) {
        logger.info("Recherche vocale de produits disponibles de type: " + typeProduit);

        List<Produit> produits = produitRepository.findByTypeProduitAndQuantiteGreaterThan(typeProduit, 0);

        return produits.stream()
                .map(p -> modelMapper.map(p, ProduitDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Rechercher des produits par nom (vocal)
     */
    public List<ProduitDTO> rechercherProduitsParNom(String nom) {
        logger.info("Recherche vocale de produits par nom: " + nom);

        List<Produit> produits = produitRepository.findByNomContainingIgnoreCase(nom);

        return produits.stream()
                .map(p -> modelMapper.map(p, ProduitDTO.class))
                .collect(Collectors.toList());
    }

    //================== COMMUNICATION VOCALE ==================

    /**
     * Envoyer un message vocal à une autre femme
     */
    @Transactional
    public ChatVocalDTO envoyerMessageVocal(Long expediteurId, Long destinataireId, String audioUrl) {
        logger.info("Femme " + expediteurId + " envoie un message vocal à la femme " + destinataireId);

        FemmeRurale expediteur = femmeRuraleRepository.findById(expediteurId)
                .orElseThrow(() -> new NotFoundException("Expéditeur non trouvé avec l'ID: " + expediteurId));

        FemmeRurale destinataire = femmeRuraleRepository.findById(destinataireId)
                .orElseThrow(() -> new NotFoundException("Destinataire non trouvé avec l'ID: " + destinataireId));

        ChatVocal message = new ChatVocal();
        message.setAudioUrl(audioUrl);
        message.setExpediteur(expediteur);
        message.setDestinataire(destinataire);
        message.setCoperative(null); // Message privé
        message.setDateEnvoi(LocalDateTime.now());
        message.setLu(false);

        ChatVocal saved = chatVocalRepository.save(message);

        logger.info("Message vocal envoyé avec succès de " + expediteurId + " à " + destinataireId);

        return toChatVocalDTO(saved);
    }

    @Transactional
    public ChatVocalDTO envoyerMessageTexteCooperative(Long femmeId, Long cooperativeId, String texte) {
        FemmeRurale femme = femmeRuraleRepository.findById(femmeId)
                .orElseThrow(() -> new NotFoundException("Femme rurale non trouvée avec l'ID: " + femmeId));

        Coperative cooperative = cooperativeRepository.findById(cooperativeId)
                .orElseThrow(() -> new NotFoundException("Coopérative non trouvée avec l'ID: " + cooperativeId));

        if (!appartenanceRepository.existsByCoperativeIdAndFemmeRuraleId(cooperativeId, femmeId)) {
            throw new IllegalArgumentException("Vous devez être membre de cette coopérative pour envoyer un message");
        }

        ChatVocal message = new ChatVocal();
        message.setExpediteur(femme);
        message.setCoperative(cooperative);
        message.setDestinataire(null);
        message.setAudioUrl(null);          // 🔹 pas d'audio
        message.setDuree(null);
        message.setTexte(texte);            // 🔹 contenu texte
        message.setDateEnvoi(LocalDateTime.now());
        message.setLu(false);

        ChatVocal saved = chatVocalRepository.save(message);
        return toChatVocalDTO(saved);
    }


    /**
     * Récupérer l'historique de chat vocal entre deux femmes
     */
    public List<ChatVocalDTO> getHistoriqueChatVocal(Long femme1Id, Long femme2Id) {
        logger.info("Récupération de l'historique de chat vocal entre " + femme1Id + " et " + femme2Id);

        FemmeRurale femme1 = femmeRuraleRepository.findById(femme1Id)
                .orElseThrow(() -> new NotFoundException("Femme non trouvée avec l'ID: " + femme1Id));

        FemmeRurale femme2 = femmeRuraleRepository.findById(femme2Id)
                .orElseThrow(() -> new NotFoundException("Femme non trouvée avec l'ID: " + femme2Id));

        List<ChatVocal> messages = chatVocalRepository.findMessagesBetweenUsers(femme1Id, femme2Id);

        return messages.stream()
                .map(this::toChatVocalDTO)
                .collect(Collectors.toList());
    }

    /**
     * Marquer un message vocal comme lu
     */
    @Transactional
    public ChatVocalDTO marquerMessageCommeLu(Long messageId) {
        logger.info("Marquage du message vocal " + messageId + " comme lu");

        ChatVocal message = chatVocalRepository.findById(messageId)
                .orElseThrow(() -> new NotFoundException("Message non trouvé avec l'ID: " + messageId));

        message.setLu(true);
        message.setDateLecture(LocalDateTime.now());

        ChatVocal updated = chatVocalRepository.save(message);

        logger.info("Message " + messageId + " marqué comme lu");

        return toChatVocalDTO(updated);
    }

    //================== GESTION DES COOPÉRATIVES ==================

    /**
     * Créer une coopérative
     */
    @Transactional
    public CoperativeDTO creerCooperative(Long femmeId, String nom, String description) {
        logger.info("Femme " + femmeId + " crée une coopérative: " + nom);

        FemmeRurale femme = femmeRuraleRepository.findById(femmeId)
                .orElseThrow(() -> new NotFoundException("Femme rurale non trouvée avec l'ID: " + femmeId));

        Coperative cooperative = new Coperative();
        cooperative.setNom(nom);
        cooperative.setDescription(description);
        cooperative.setNbrMembres(1); // La créatrice est le premier membre

        Coperative saved = cooperativeRepository.save(cooperative);

        // Créer l'appartenance
        Appartenance appartenance = new Appartenance();
        appartenance.setCoperative(saved);
        appartenance.setFemmeRurale(femme);
        appartenance.setDateAdhesion(LocalDateTime.now());

        appartenanceRepository.save(appartenance);

        // Enregistrer dans l'historique
        historiqueService.enregistrerAdhesionCooperative(femme, saved, true);

        logger.info("Coopérative '" + nom + "' créée avec succès par la femme " + femmeId);

        return modelMapper.map(saved, CoperativeDTO.class);
    }

    /**
     * Rejoindre une coopérative
     */
    @Transactional
    public void rejoindreCooperative(Long femmeId, Long cooperativeId) {
        FemmeRurale femme = femmeRuraleRepository.findById(femmeId)
                .orElseThrow(() -> new NotFoundException("Femme rurale non trouvée avec l'ID: " + femmeId));

        Coperative cooperative = cooperativeRepository.findById(cooperativeId)
                .orElseThrow(() -> new NotFoundException("Coopérative non trouvée avec l'ID: " + cooperativeId));

        // Vérifier si l'appartenance existe déjà
        if (appartenanceRepository.existsByCoperativeIdAndFemmeRuraleId(cooperativeId, femmeId)) {
            throw new IllegalArgumentException("Vous êtes déjà membre de cette coopérative");
        }

        // Créer l'appartenance
        Appartenance appartenance = new Appartenance();
        appartenance.setCoperative(cooperative);
        appartenance.setFemmeRurale(femme);
        appartenance.setDateAdhesion(LocalDateTime.now());

        appartenanceRepository.save(appartenance);

        // Incrémenter le nombre de membres
        cooperative.setNbrMembres(cooperative.getNbrMembres() + 1);
        cooperativeRepository.save(cooperative);

        // Enregistrer dans l'historique
        historiqueService.enregistrerAdhesionCooperative(femme, cooperative, false);

        // Notifier les autres membres
        List<FemmeRurale> membres = appartenanceRepository.findFemmeRuralesByCoperativeId(cooperativeId);
        List<Utilisateur> utilisateurs = new ArrayList<>();
        for (FemmeRurale membre : membres) {
            if (!membre.getId().equals(femmeId)) {
                utilisateurs.add(membre);
            }
        }

        notificationService.notifierNouveauMembreCooperative(cooperative, femme, utilisateurs);

        logger.info("Femme " + femmeId + " a rejoint la coopérative '" + cooperative.getNom() + "' avec succès");
    }

    /**
     * Joindre une coopérative (version DTO)
     */
    @Transactional
    public AppartenanceDTO joindreCooperative(Long femmeId, Long cooperativeId) {
        rejoindreCooperative(femmeId, cooperativeId);

        // Récupérer l'appartenance créée
        Appartenance appartenance = appartenanceRepository.findByCoperativeIdAndFemmeRuraleId(cooperativeId, femmeId)
                .orElseThrow(() -> new NotFoundException("Appartenance non trouvée"));

        return modelMapper.map(appartenance, AppartenanceDTO.class);
    }

    /**
     * Quitter une coopérative
     */
    @Transactional
    public void quitterCooperative(Long femmeId, Long cooperativeId) {
        FemmeRurale femme = femmeRuraleRepository.findById(femmeId)
                .orElseThrow(() -> new NotFoundException("Femme rurale non trouvée avec l'ID: " + femmeId));

        Coperative cooperative = cooperativeRepository.findById(cooperativeId)
                .orElseThrow(() -> new NotFoundException("Coopérative non trouvée avec l'ID: " + cooperativeId));

        // Vérifier si l'appartenance existe
        Appartenance appartenance = appartenanceRepository.findByCoperativeIdAndFemmeRuraleId(cooperativeId, femmeId)
                .orElseThrow(() -> new IllegalArgumentException("Vous n'êtes pas membre de cette coopérative"));

        // Supprimer l'appartenance
        appartenanceRepository.delete(appartenance);

        // Décrémenter le nombre de membres
        cooperative.setNbrMembres(Math.max(0, cooperative.getNbrMembres() - 1));
        cooperativeRepository.save(cooperative);

        logger.info("Femme " + femmeId + " a quitté la coopérative '" + cooperative.getNom() + "'");
    }

    @Transactional
    public ChatVocalDTO envoyerMessageVocalCooperative(Long femmeId, Long cooperativeId, String audioUrl) {
        FemmeRurale femme = femmeRuraleRepository.findById(femmeId)
                .orElseThrow(() -> new NotFoundException("Femme rurale non trouvée avec l'ID: " + femmeId));

        Coperative cooperative = cooperativeRepository.findById(cooperativeId)
                .orElseThrow(() -> new NotFoundException("Coopérative non trouvée avec l'ID: " + cooperativeId));

        // Vérifier que la femme est membre
        if (!appartenanceRepository.existsByCoperativeIdAndFemmeRuraleId(cooperativeId, femmeId)) {
            throw new IllegalArgumentException("Vous devez être membre de cette coopérative pour envoyer un message");
        }

        ChatVocal message = new ChatVocal();
        message.setAudioUrl(audioUrl);
        message.setExpediteur(femme);
        message.setCoperative(cooperative);
        message.setDestinataire(null); // message à la coopérative
        message.setDateEnvoi(LocalDateTime.now());
        message.setLu(false);

        ChatVocal saved = chatVocalRepository.save(message);

        logger.info("Message vocal envoyé dans la coopérative '" + cooperative.getNom() + "' par la femme " + femmeId);

        return toChatVocalDTO(saved);
    }

    public List<ChatVocalDTO> getMessagesCooperative(Long cooperativeId) {
        Coperative cooperative = cooperativeRepository.findById(cooperativeId)
                .orElseThrow(() -> new NotFoundException("Coopérative non trouvée avec l'ID: " + cooperativeId));

        List<ChatVocal> messages = chatVocalRepository.findByCoperativeId(cooperativeId);

        return messages.stream()
                .map(this::toChatVocalDTO)
                .collect(Collectors.toList());
    }


    /**
     * Compter les messages non lus d'une femme
     */
    public Long compterMessagesNonLus(Long femmeId) {
        FemmeRurale femme = femmeRuraleRepository.findById(femmeId)
                .orElseThrow(() -> new NotFoundException("Femme rurale non trouvée avec l'ID: " + femmeId));

        return chatVocalRepository.countUnreadMessagesByFemmeId(femmeId);
    }

    /**
     * Lister les coopératives d'une femme rurale
     */
    public List<CoperativeDTO> listerMesCooperatives(Long femmeId) {
        logger.info("Récupération des coopératives de la femme " + femmeId);

        // 1. Vérifier que la femme existe
        FemmeRurale femme = femmeRuraleRepository.findById(femmeId)
                .orElseThrow(() ->
                        new NotFoundException("Femme rurale non trouvée avec l'ID: " + femmeId)
                );

        // 2. Récupérer toutes les appartenances de cette femme
        List<Appartenance> appartenances = appartenanceRepository.findByFemmeRurale(femme);

        // 3. Mapper chaque coopérative liée en CoperativeDTO
        return appartenances.stream()
                .map(a -> {
                    Coperative coop = a.getCoperative();
                    return modelMapper.map(coop, CoperativeDTO.class);
                })
                .collect(Collectors.toList());
    }

    //================== GESTION DES AUDIOS DE COOPÉRATIVE ==================

    /**
     * Sauvegarder physiquement un audio de coopérative sur le serveur
     * et retourner l'URL relative à stocker dans ChatVocal.audioUrl.
     */
    @Transactional
    public String sauvegarderAudioCooperative(Long femmeId, MultipartFile audioFile) throws IOException {
        logger.info("Femme " + femmeId + " upload un audio de coopérative : "
                + (audioFile != null ? audioFile.getOriginalFilename() : "null"));

        if (audioFile == null || audioFile.isEmpty()) {
            throw new IllegalArgumentException("Le fichier audio est vide");
        }

        // Vérifier que la femme existe (cohérence métier)
        FemmeRurale femme = femmeRuraleRepository.findById(femmeId)
                .orElseThrow(() -> new NotFoundException("Femme rurale non trouvée avec l'ID: " + femmeId));

        String originalFilename = StringUtils.cleanPath(audioFile.getOriginalFilename());
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex >= 0) {
            extension = originalFilename.substring(dotIndex); // .m4a, .aac, .mp3, ...
        }

        // Nom unique basé sur l'ID de la femme + timestamp
        String baseName = "coop_audio_" + femme.getId() + "_" + System.currentTimeMillis();
        String fileName = baseName + extension;

        // Dossier "uploads/audios" à la racine du projet
        Path uploadsDir = Paths.get("uploads", "audios");
        if (!Files.exists(uploadsDir)) {
            Files.createDirectories(uploadsDir);
        }

        Path target = uploadsDir.resolve(fileName);
        Files.copy(audioFile.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        logger.info("Audio de coopérative sauvegardé sur le serveur : " + fileName);

        // Tu peux renvoyer soit juste le nom, soit l'URL relative
        return "/uploads/audios/" + fileName;
    }


    //================== GESTION DES COMMANDES ==================

    /**
     * Mapping Produit -> ProduitDTO (en enrichissant avec femmeRuraleId)
     */
    private ProduitDTO toProduitDto(Produit produit) {
        if (produit == null) {
            return null;
        }

        // On utilise ModelMapper pour les champs de base
        ProduitDTO dto = modelMapper.map(produit, ProduitDTO.class);

        if (produit.getFemmeRurale() != null) {
            dto.setFemmeRuraleId(produit.getFemmeRurale().getId());
        }

        return dto;
    }

    /**
     * Mapping Commande -> CommandeDTO avec produit + vendeuse
     */
    private CommandeDTO toCommandeDto(Commande commande) {
        if (commande == null) {
            return null;
        }

        // Base : on laisse ModelMapper faire le gros du travail
        CommandeDTO dto = modelMapper.map(commande, CommandeDTO.class);

        // Sécurise le montantTotal (au cas où)
        dto.setMontantTotal(commande.getMontantTotal());

        // Ajout du produit
        Produit produit = commande.getProduit();
        if (produit != null) {
            dto.setProduitId(produit.getId());
            dto.setProduit(toProduitDto(produit));

            // Ajout des infos vendeuse
            FemmeRurale vendeuse = produit.getFemmeRurale();
            if (vendeuse != null) {
                dto.setVendeuseId(vendeuse.getId());

                String nomComplet =
                        (vendeuse.getPrenom() != null ? vendeuse.getPrenom() + " " : "") +
                                (vendeuse.getNom() != null ? vendeuse.getNom() : "");
                nomComplet = nomComplet.trim();

                if (!nomComplet.isEmpty()) {
                    dto.setVendeuseNom(nomComplet);
                }
            }
        }

        // Ajout de l’acheteurId (par sécurité)
        if (commande.getAcheteur() != null) {
            dto.setAcheteurId(commande.getAcheteur().getId());
        }

        return dto;
    }


    /**
     * Passer une commande
     */
    @Transactional
    public CommandeDTO passerCommande(Long acheteurId, Long produitId, Integer quantite) {
        logger.info("Acheteur " + acheteurId + " passe une commande pour le produit "
                + produitId + " (quantité: " + quantite + ")");

        // 0) Vérification quantité
        if (quantite == null || quantite <= 0) {
            throw new IllegalArgumentException("La quantité doit être supérieure à 0");
        }

        // 1) Récupérer l'acheteur (FemmeRurale OU Acheteur standard)
        Utilisateur acheteur = null;

        FemmeRurale femmeAcheteur = femmeRuraleRepository.findById(acheteurId).orElse(null);
        if (femmeAcheteur != null) {
            acheteur = femmeAcheteur;
        } else {
            Acheteur acheteurStandard = acheteurRepository.findById(acheteurId).orElse(null);
            if (acheteurStandard != null) {
                acheteur = acheteurStandard;
            }
        }

        if (acheteur == null) {
            throw new NotFoundException("Acheteur non trouvé avec l'ID: " + acheteurId);
        }

        // 2) Récupérer le produit
        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new NotFoundException("Produit non trouvé avec l'ID: " + produitId));

        // 3) Interdiction d’acheter son propre produit (si acheteur est une FemmeRurale)
        if (acheteur instanceof FemmeRurale) {
            FemmeRurale vendeuse = produit.getFemmeRurale();
            if (vendeuse != null && vendeuse.getId().equals(acheteur.getId())) {
                throw new IllegalArgumentException("Vous ne pouvez pas acheter votre propre produit.");
            }
        }

        // 4) Vérifier le stock
        Integer stock = produit.getQuantite();
        if (stock == null) {
            stock = 0;
        }

        if (stock < quantite) {
            throw new IllegalArgumentException(
                    "Stock insuffisant. Disponible: " + stock + ", demandé: " + quantite);
        }

        // 5) Créer la commande
        Commande commande = new Commande();
        commande.setQuantite(quantite);
        commande.setStatutCommande(StatutCommande.EN_ATTENTE);
        commande.setDateAchat(LocalDateTime.now());
        commande.setAcheteur(acheteur);
        commande.setProduit(produit);

        Commande saved = commandeRepository.save(commande);

        // 6) Mettre à jour le stock
        produit.setQuantite(stock - quantite);
        produitRepository.save(produit);

        // 7) Enregistrer dans l'historique + notif
        historiqueService.enregistrerAchat(acheteur, saved, produit);
        notificationService.notifierNouvelleCommande(saved);

        logger.info("Commande " + saved.getId() + " passée avec succès pour le produit " + produitId);

        // 8) Retourner un DTO enrichi (produit + vendeuse)
        return toCommandeDto(saved);
    }


    /**
     * Récupérer les commandes d'un utilisateur
     */
    public List<CommandeDTO> getCommandesUtilisateur(Long utilisateurId) {
        logger.info("Récupération des commandes de l'utilisateur " + utilisateurId);

        List<Commande> commandes = commandeRepository.findByAcheteurId(utilisateurId);

        return commandes.stream()
                .map(this::toCommandeDto)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer les commandes d'une femme rurale (en tant qu'acheteur OU vendeuse)
     */
    public List<CommandeDTO> getCommandesFemmeRurale(Long femmeId) {
        logger.info("Récupération des commandes de la femme rurale " + femmeId);

        List<Commande> commandesAsAcheteur = commandeRepository.findByAcheteurId(femmeId);
        List<Commande> commandesAsVendeur = commandeRepository.findByProduit_FemmeRurale_Id(femmeId);

        // Combiner les deux listes
        List<Commande> allCommandes = new ArrayList<>();
        allCommandes.addAll(commandesAsAcheteur);
        allCommandes.addAll(commandesAsVendeur);

        // Supprimer les doublons par ID
        Set<Long> seenIds = new HashSet<>();
        allCommandes.removeIf(c -> !seenIds.add(c.getId()));

        return allCommandes.stream()
                .map(this::toCommandeDto)
                .collect(Collectors.toList());
    }


    /**
     * Mettre à jour le statut d'une commande (admin)
     */
    @Transactional
    public CommandeDTO mettreAJourStatutCommande(Long commandeId, String statut) {
        logger.info("Mise à jour du statut de la commande " + commandeId + " vers " + statut);

        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new NotFoundException("Commande non trouvée avec l'ID: " + commandeId));

        try {
            StatutCommande statutCommande = StatutCommande.valueOf(statut.toUpperCase());
            commande.setStatutCommande(statutCommande);
            Commande updated = commandeRepository.save(commande);

            // Notifier l'acheteur du changement de statut
            notificationService.notifierChangementStatutCommande(updated);

            logger.info("Statut de la commande " + commandeId + " mis à jour vers " + statut);
            return modelMapper.map(updated, CommandeDTO.class);
        } catch (IllegalArgumentException e) {
            logger.warning("Statut invalide: " + statut);
            throw new IllegalArgumentException("Statut invalide. Valeurs acceptées: EN_ATTENTE, EN_COURS, LIVRE, ANNULE");
        }
    }

    //================== GESTION DES PAIEMENTS ==================

    /**
     * Effectuer un paiement
     */
    @Transactional
    public PaiementDTO effectuerPaiement(Long commandeId, ModePaiement modePaiement) {
        logger.info("Paiement de la commande " + commandeId + " via " + modePaiement);

        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new NotFoundException("Commande non trouvée avec l'ID: " + commandeId));

        // Vérifier que la commande n'est pas déjà payée
        if (!commande.getPaiement().isEmpty()) {
            throw new IllegalArgumentException("Cette commande est déjà payée");
        }

        // Créer le paiement
        Paiement paiement = new Paiement();
        paiement.setModePaiement(modePaiement);
        paiement.setMontant(commande.getProduit().getPrix() * commande.getQuantite());
        paiement.setAcheteur(commande.getAcheteur());
        paiement.setCommande(commande);
        paiement.setDatePaiement(LocalDateTime.now());

        Paiement saved = paiementRepository.save(paiement);

        // Mettre à jour le statut de la commande
        commande.setStatutCommande(StatutCommande.EN_COURS);
        commandeRepository.save(commande);

        // Enregistrer dans l'historique
        historiqueService.enregistrerPaiement(commande.getAcheteur(), saved, commande);

        // Notifier le vendeur
        notificationService.notifierPaiementRecu(saved);

        logger.info("Paiement " + saved.getId() + " effectué avec succès pour la commande " + commandeId);

        return modelMapper.map(saved, PaiementDTO.class);
    }

    /**
     * Récupérer les paiements d'un utilisateur
     */
    public List<PaiementDTO> getPaiementsUtilisateur(Long utilisateurId) {
        logger.info("Récupération des paiements de l'utilisateur " + utilisateurId);

        List<Paiement> paiements = paiementRepository.findByAcheteurId(utilisateurId);

        return paiements.stream()
                .map(p -> modelMapper.map(p, PaiementDTO.class))
                .collect(Collectors.toList());
    }

    //================== RECHERCHE PAR LOCALISATION ==================

    /**
     * Rechercher des femmes rurales par localisation
     */
    @Transactional
    public List<FemmeRuraleDTO> rechercherParLocalisation(Long acheteurId, String localite) {
        logger.info("Acheteur " + acheteurId + " recherche des femmes rurales dans la localité: " + localite);

        Acheteur acheteur = acheteurRepository.findById(acheteurId)
                .orElseThrow(() -> new NotFoundException("Acheteur non trouvé avec l'ID: " + acheteurId));

        List<FemmeRurale> femmes = femmeRuraleRepository.findByLocalite(localite);

        // Enregistrer la recherche
        RechercherParLocalisation recherche = new RechercherParLocalisation();
        recherche.setAcheteur(acheteur);
        recherche.setFemmeRurale(null); // Pour l'instant, on ne sait pas quelle femme sera choisie
        recherche.setLocalite(localite);
        recherche.setDateRecherche(LocalDateTime.now());

        rechercherParLocalisationRepository.save(recherche);

        return femmes.stream()
                .map(fr -> modelMapper.map(fr, FemmeRuraleDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Contacter une femme rurale trouvée par localisation
     */
    @Transactional
    public void contacterFemmeRurale(Long acheteurId, Long femmeId) {
        logger.info("Acheteur " + acheteurId + " contacte la femme rurale " + femmeId);

        Acheteur acheteur = acheteurRepository.findById(acheteurId)
                .orElseThrow(() -> new NotFoundException("Acheteur non trouvé avec l'ID: " + acheteurId));

        FemmeRurale femme = femmeRuraleRepository.findById(femmeId)
                .orElseThrow(() -> new NotFoundException("Femme rurale non trouvée avec l'ID: " + femmeId));

        // Mettre à jour la recherche avec la femme contactée
        List<RechercherParLocalisation> recherches = rechercherParLocalisationRepository.findByAcheteurIdOrderByDateRechercheDesc(acheteurId);
        if (!recherches.isEmpty()) {
            RechercherParLocalisation recherche = recherches.get(0); // Get the latest search
            recherche.setFemmeRurale(femme);
            rechercherParLocalisationRepository.save(recherche);
        }

        logger.info("Acheteur " + acheteurId + " a contacté la femme rurale " + femmeId);
    }

    //================== PARTAGE DE CONTENUS ==================

    /**
     * Partager un contenu dans une coopérative
     */
    @Transactional
    public PartageCooperativeDTO partagerContenu(Long femmeId, Long contenuId, Long cooperativeId, String messageAudioUrl) {
        logger.info("Femme " + femmeId + " partage le contenu " + contenuId + " dans la coopérative " + cooperativeId);

        FemmeRurale femme = femmeRuraleRepository.findById(femmeId)
                .orElseThrow(() -> new NotFoundException("Femme rurale non trouvée avec l'ID: " + femmeId));

        Contenu contenu = contenuRepository.findById(contenuId)
                .orElseThrow(() -> new NotFoundException("Contenu non trouvé avec l'ID: " + contenuId));

        Coperative cooperative = cooperativeRepository.findById(cooperativeId)
                .orElseThrow(() -> new NotFoundException("Coopérative non trouvée avec l'ID: " + cooperativeId));

        // Vérifier que la femme est membre de la coopérative
        if (!appartenanceRepository.existsByCoperativeIdAndFemmeRuraleId(cooperativeId, femmeId)) {
            throw new IllegalArgumentException("Vous devez être membre de cette coopérative pour partager un contenu");
        }

        PartageCooperative partage = new PartageCooperative();
        partage.setContenu(contenu);
        partage.setCoperative(cooperative);
        partage.setPartagePar(femme);
        partage.setDatePartage(LocalDateTime.now());
        partage.setMessageAudioUrl(messageAudioUrl);

        PartageCooperative saved = partageCooperativeRepository.save(partage);

        // Enregistrer dans l'historique
        historiqueService.enregistrerPartage(femme, contenu, cooperative);

        // Notifier les membres de la coopérative
        List<FemmeRurale> membres = appartenanceRepository.findFemmeRuralesByCoperativeId(cooperativeId);
        List<Utilisateur> utilisateurs = new ArrayList<>();
        for (FemmeRurale membre : membres) {
            if (!membre.getId().equals(femmeId)) {
                utilisateurs.add(membre);
            }
        }

        notificationService.notifierContenuPartage(contenu, cooperative, utilisateurs);

        logger.info("Contenu '" + contenu.getTitre() + "' partagé avec succès dans la coopérative '" + cooperative.getNom() + "' par la femme " + femmeId);

        return modelMapper.map(saved, PartageCooperativeDTO.class);
    }

    /**
     * Partager un contenu dans une coopérative (alias)
     */
    @Transactional
    public PartageCooperativeDTO partagerContenuDansCooperative(Long femmeId, Long cooperativeId, Long contenuId, String messageAudioUrl) {
        return partagerContenu(femmeId, contenuId, cooperativeId, messageAudioUrl);
    }

    /**
     * Récupérer les contenus partagés dans une coopérative
     */
    public List<PartageCooperativeDTO> getContenusPartages(Long cooperativeId, String typeInfo) {
        logger.info("Récupération des contenus partagés (type: " + typeInfo + ") dans la coopérative " + cooperativeId);

        Coperative cooperative = cooperativeRepository.findById(cooperativeId)
                .orElseThrow(() -> new NotFoundException("Coopérative non trouvée avec l'ID: " + cooperativeId));

        List<PartageCooperative> partages;
        if (typeInfo != null && !typeInfo.isEmpty()) {
            try {
                TypeInfo type = TypeInfo.valueOf(typeInfo.toUpperCase());
                partages = partageCooperativeRepository.findByCoperativeIdAndContenu_TypeInfo(cooperativeId, type);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("TypeInfo invalide. Valeurs acceptées: SANTE, NUTRITION, DROIT, VIDEO_FORMATION");
            }
        } else {
            partages = partageCooperativeRepository.findByCoperativeId(cooperativeId);
        }

        return partages.stream()
                .map(pc -> modelMapper.map(pc, PartageCooperativeDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Récupérer les contenus partagés dans une coopérative (alias)
     */
    public List<PartageCooperativeDTO> getContenusPartagesDansCooperative(Long cooperativeId) {
        return getContenusPartages(cooperativeId, null);
    }

    /**
     * Récupérer les contenus partagés filtrés par TypeInfo (alias)
     */
    public List<PartageCooperativeDTO> getContenusPartagesParType(Long cooperativeId, String typeInfo) {
        return getContenusPartages(cooperativeId, typeInfo);
    }

    /**
     * Récupérer mes partages
     */
    public List<PartageCooperativeDTO> getMesPartages(Long femmeId) {
        logger.info("Récupération des partages de la femme " + femmeId);

        FemmeRurale femme = femmeRuraleRepository.findById(femmeId)
                .orElseThrow(() -> new NotFoundException("Femme rurale non trouvée avec l'ID: " + femmeId));

        List<PartageCooperative> partages = partageCooperativeRepository.findByUser(femme);

        return partages.stream()
                .map(pc -> modelMapper.map(pc, PartageCooperativeDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Supprimer un partage
     */
    @Transactional
    public void supprimerPartage(Long femmeId, Long partageId) {
        logger.info("Suppression du partage " + partageId + " par la femme " + femmeId);

        FemmeRurale femme = femmeRuraleRepository.findById(femmeId)
                .orElseThrow(() -> new NotFoundException("Femme rurale non trouvée avec l'ID: " + femmeId));

        PartageCooperative partage = partageCooperativeRepository.findById(partageId)
                .orElseThrow(() -> new NotFoundException("Partage non trouvé avec l'ID: " + partageId));

        // Vérifier que le partage appartient à cette femme
        if (!partage.getPartagePar().getId().equals(femmeId)) {
            throw new IllegalArgumentException("Vous ne pouvez supprimer que vos propres partages");
        }

        partageCooperativeRepository.delete(partage);

        logger.info("Partage " + partageId + " supprimé avec succès");
    }

    private ChatVocalDTO toChatVocalDTO(ChatVocal entity) {
        if (entity == null) {
            return null;
        }

        FemmeRurale expediteur = entity.getExpediteur();
        FemmeRurale destinataire = entity.getDestinataire();
        Coperative cooperative = entity.getCoperative();

        return ChatVocalDTO.builder()
                .id(entity.getId())
                .audioUrl(entity.getAudioUrl())
                .duree(entity.getDuree()) // si tu as ce champ dans ChatVocal, sinon enlève cette ligne

                .expediteurId(expediteur != null ? expediteur.getId() : null)
                .expediteurNom(expediteur != null ? expediteur.getNom() : null)
                .expediteurPrenom(expediteur != null ? expediteur.getPrenom() : null)

                .destinataireId(destinataire != null ? destinataire.getId() : null)
                .destinataireNom(destinataire != null ? destinataire.getNom() : null)
                .destinatairePrenom(destinataire != null ? destinataire.getPrenom() : null)

                .cooperativeId(cooperative != null ? cooperative.getId() : null)
                .cooperativeNom(cooperative != null ? cooperative.getNom() : null)

                .dateEnvoi(entity.getDateEnvoi())
                .lu(entity.isLu())
                .dateLecture(entity.getDateLecture())
                .build();
    }


    //================== GETTERS POUR LES REPOSITORIES ==================
    // Nécessaires pour les tests et l'injection de dépendances

    public FemmeRuraleRepository getFemmeRuraleRepository() {
        return femmeRuraleRepository;
    }

    public ProduitRepository getProduitRepository() {
        return produitRepository;
    }

    public ContenuRepository getContenuRepository() {
        return contenuRepository;
    }

    public CommandeRepository getCommandeRepository() {
        return commandeRepository;
    }

    public PaiementRepository getPaiementRepository() {
        return paiementRepository;
    }

    public CoperativeRepository getCooperativeRepository() {
        return cooperativeRepository;
    }

    public AppartenanceRepository getAppartenanceRepository() {
        return appartenanceRepository;
    }

    public ChatVocalRepository getChatVocalRepository() {
        return chatVocalRepository;
    }

    public UtilisateurAudioRepository getUtilisateurAudioRepository() {
        return utilisateurAudioRepository;
    }

    public PartageCooperativeRepository getPartageCooperativeRepository() {
        return partageCooperativeRepository;
    }

    public RechercherParLocalisationRepository getRechercherParLocalisationRepository() {
        return rechercherParLocalisationRepository;
    }

    public AcheteurRepository getAcheteurRepository() {
        return acheteurRepository;
    }
}
