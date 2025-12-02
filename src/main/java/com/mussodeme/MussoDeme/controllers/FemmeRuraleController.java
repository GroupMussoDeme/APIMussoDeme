package com.mussodeme.MussoDeme.controllers;

import com.mussodeme.MussoDeme.dto.*;
import com.mussodeme.MussoDeme.enums.ModePaiement;
import com.mussodeme.MussoDeme.enums.TypeProduit;
import com.mussodeme.MussoDeme.services.FemmeRuraleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Controller REST pour les Femmes Rurales
 * Interface audio avec pictogrammes
 *
 * IMPORTANT: Les femmes rurales utilisent l'application uniquement par audio
 * - Elles écoutent les informations (santé, nutrition, droit...)
 * - Elles enregistrent leurs commandes vocales
 * - Elles manipulent par pictogrammes
 */
@RestController
@RequestMapping("/api/femmes-rurales")
public class FemmeRuraleController {

    private static final Logger logger = Logger.getLogger(FemmeRuraleController.class.getName());

    private final FemmeRuraleService femmeRuraleService;

    // Constructor for dependency injection
    public FemmeRuraleController(FemmeRuraleService femmeRuraleService) {
        this.femmeRuraleService = femmeRuraleService;
    }

    //================== GESTION DES CONTENUS (AUDIO/VIDEO) ==================

    /**
     * Écouter un contenu audio ou vidéo
     * POST /api/femmes-rurales/{femmeId}/contenus/{contenuId}/ecouter
     */
    @PostMapping("/{femmeId}/contenus/{contenuId}/ecouter")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> ecouterContenu(
            @PathVariable Long femmeId,
            @PathVariable Long contenuId) {

        logger.info("[API] Femme " + femmeId + " écoute le contenu " + contenuId);

        ContenuDTO contenu = femmeRuraleService.ecouterContenu(femmeId, contenuId);

        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Écoute du contenu enregistrée avec succès")
                        .data(contenu)
                        .build()
        );
    }

    /**
     * Upload d'une image de produit pour une femme rurale
     * POST /api/femmes-rurales/{femmeId}/produits/upload-image
     */
    @PostMapping("/{femmeId}/produits/upload-image")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> uploadProduitImage(
            @PathVariable Long femmeId,
            @RequestParam("image") MultipartFile imageFile) throws IOException {

        logger.info("[API] Femme " + femmeId + " upload l'image d'un produit : "
                + imageFile.getOriginalFilename());

        // Nom de fichier généré par le service
        String storedFileName = femmeRuraleService.sauvegarderImageProduit(femmeId, imageFile);

        // ⚠️ C'est ÇA qu'il faut renvoyer pour Flutter
        Map<String, Object> payload = new HashMap<>();
        payload.put("fileName", storedFileName);
        payload.put("url", "/uploads/" + storedFileName);

        return ResponseEntity.ok(
                Response.builder()
                        .status(200) // ou HttpStatus.OK.value()
                        .message("Image uploadée avec succès")
                        .data(payload)                    // ⚠️ IMPORTANT
                        .build()
        );
    }
    /**
     * Lister tous les audios disponibles
     * GET /api/femmes-rurales/contenus/audios
     */
    @GetMapping("/contenus/audios")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> listerAudios() {
        logger.info("[API] Liste de tous les audios demandée");

        List<ContenuDTO> audios = femmeRuraleService.listerAudios();

        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(audios.size() + " audio(s) trouvé(s)")
                        .data(audios)
                        .build()
        );
    }

    /**
     * Lister toutes les vidéos disponibles
     * GET /api/femmes-rurales/contenus/videos
     */
    @GetMapping("/contenus/videos")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> listerVideos() {
        logger.info("[API] Liste de toutes les vidéos demandée");

        List<ContenuDTO> videos = femmeRuraleService.listerVideos();

        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(videos.size() + " vidéo(s) trouvée(s)")
                        .data(videos)
                        .build()
        );
    }

    /**
     * Lister les contenus par TypeInfo (SANTE, NUTRITION, DROIT, VIDEO_FORMATION)
     * GET /api/femmes-rurales/contenus/type/{typeInfo}
     */
    @GetMapping("/contenus/type/{typeInfo}")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> listerContenusParTypeInfo(@PathVariable String typeInfo) {
        logger.info("[API] Liste des contenus par TypeInfo: " + typeInfo);

        List<ContenuDTO> contenus = femmeRuraleService.listerContenusParTypeInfo(typeInfo);

        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(contenus.size() + " contenu(s) de type " + typeInfo + " trouvé(s)")
                        .data(contenus)
                        .build()
        );
    }

    /**
     * Historique de lecture d'une femme
     * GET /api/femmes-rurales/{femmeId}/historique-lecture
     */
    @GetMapping("/{femmeId}/historique-lecture")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> getHistoriqueLecture(@PathVariable Long femmeId) {
        logger.info("[API] Historique de lecture demandé pour la femme " + femmeId);

        List<UtilisateurAudioDTO> historique = femmeRuraleService.getHistoriqueLecture(femmeId);

        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Historique de lecture récupéré")
                        .data(historique)
                        .build()
        );
    }

    //================== GESTION DES PRODUITS ==================

    /**
     * Publier un produit avec audio de guide
     * POST /api/femmes-rurales/{femmeId}/produits
     */
    @PostMapping("/{femmeId}/produits")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> publierProduit(
            @PathVariable Long femmeId,
            @Valid @RequestBody ProduitDTO produitDTO) {

        logger.info("[API] Femme " + femmeId + " publie un nouveau produit");

        ProduitDTO produit = femmeRuraleService.publierProduit(femmeId, produitDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Response.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Produit publié avec succès")
                        .data(produit)
                        .build()
        );
    }

    /**
     * Voir ses propres produits
     * GET /api/femmes-rurales/{femmeId}/mes-produits
     */
    @GetMapping("/{femmeId}/mes-produits")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> voirMesProduits(@PathVariable Long femmeId) {
        logger.info("[API] Femme " + femmeId + " consulte ses produits");

        List<ProduitDTO> produits = femmeRuraleService.voirMesProduits(femmeId);

        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Mes produits récupérés")
                        .data(produits)
                        .build()
        );
    }

    /**
     * Voir tous les produits (de toutes les femmes)
     * GET /api/femmes-rurales/produits
     */
    @GetMapping("/produits")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> voirTousLesProduits() {
        logger.info("[API] Consultation de tous les produits");

        List<ProduitDTO> produits = femmeRuraleService.voirTousLesProduits();

        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(produits.size() + " produit(s) disponible(s)")
                        .data(produits)
                        .build()
        );
    }

    /**
     * Récupérer un produit avec son audio de guide
     * GET /api/femmes-rurales/produits/{produitId}
     */
    @GetMapping("/produits/{produitId}")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> getProduitAvecAudioGuide(@PathVariable Long produitId) {
        logger.info("[API] Récupération du produit " + produitId + " avec audio");

        ProduitDTO produit = femmeRuraleService.getProduitAvecAudioGuide(produitId);

        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Produit récupéré")
                        .data(produit)
                        .build()
        );
    }

    /**
     * Modifier un produit
     * PUT /api/femmes-rurales/{femmeId}/produits/{produitId}
     */
    @PutMapping("/{femmeId}/produits/{produitId}")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> modifierProduit(
            @PathVariable Long femmeId,
            @PathVariable Long produitId,
            @Valid @RequestBody ProduitDTO produitDTO) {

        logger.info("[API] Femme " + femmeId + " modifie le produit " + produitId);

        ProduitDTO produit = femmeRuraleService.modifierProduit(femmeId, produitId, produitDTO);

        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Produit modifié avec succès")
                        .data(produit)
                        .build()
        );
    }

    /**
     * Supprimer un produit
     * DELETE /api/femmes-rurales/{femmeId}/produits/{produitId}
     */
    @DeleteMapping("/{femmeId}/produits/{produitId}")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> supprimerProduit(
            @PathVariable Long femmeId,
            @PathVariable Long produitId) {

        logger.info("[API] Femme " + femmeId + " supprime le produit " + produitId);

        femmeRuraleService.supprimerProduit(femmeId, produitId);

        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Produit supprimé avec succès")
                        .build()
        );
    }

    /**
     * Recherche vocale : Trouver les produits par type
     * GET /api/femmes-rurales/produits/recherche/type/{typeProduit}
     */
    @GetMapping("/produits/recherche/type/{typeProduit}")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> chercherProduitsParType(@PathVariable String typeProduit) {
        logger.info("[API] Recherche vocale de produits par type: " + typeProduit);

        try {
            List<ProduitDTO> produits = femmeRuraleService
                    .rechercherProduitsParType(TypeProduit.valueOf(typeProduit.toUpperCase()));

            return ResponseEntity.ok(
                    Response.builder()
                            .statusCode(HttpStatus.OK.value())
                            .message(produits.size() + " produit(s) trouvé(s)")
                            .data(produits)
                            .build()
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    Response.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("Type de produit invalide. Valeurs acceptées: ARTISANAT, AGRICOLE, ALIMENTAIRE")
                            .build()
            );
        }
    }

    /**
     * Recherche vocale : Trouver les produits disponibles par type
     * GET /api/femmes-rurales/produits/recherche/disponibles/type/{typeProduit}
     */
    @GetMapping("/produits/recherche/disponibles/type/{typeProduit}")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> chercherProduitsDisponiblesParType(@PathVariable String typeProduit) {
        logger.info("[API] Recherche vocale de produits disponibles par type: " + typeProduit);

        try {
            List<ProduitDTO> produits = femmeRuraleService
                    .rechercherProduitsDisponiblesParType(TypeProduit.valueOf(typeProduit.toUpperCase()));

            return ResponseEntity.ok(
                    Response.builder()
                            .statusCode(HttpStatus.OK.value())
                            .message(produits.size() + " produit(s) disponible(s) trouvé(s)")
                            .data(produits)
                            .build()
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    Response.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("Type de produit invalide. Valeurs acceptées: ARTISANAT, AGRICOLE, ALIMENTAIRE")
                            .build()
            );
        }
    }

    /**
     * Recherche vocale : Trouver les produits par nom
     * GET /api/femmes-rurales/produits/recherche/nom/{nom}
     */
    @GetMapping("/produits/recherche/nom/{nom}")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> chercherProduitsParNom(@PathVariable String nom) {
        logger.info("[API] Recherche vocale de produits par nom: " + nom);

        List<ProduitDTO> produits = femmeRuraleService.rechercherProduitsParNom(nom);

        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(produits.size() + " produit(s) trouvé(s)")
                        .data(produits)
                        .build()
        );
    }

    //================== CHAT VOCAL ==================

    /**
     * Envoyer un message vocal privé
     * POST /api/femmes-rurales/{expediteurId}/chats/envoyer
     */
    @PostMapping("/{expediteurId}/chats/envoyer")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> envoyerMessageVocal(
            @PathVariable Long expediteurId,
            @RequestParam Long destinataireId,
            @RequestParam String audioUrl) {

        logger.info("[API] Femme " + expediteurId + " envoie un message vocal à " + destinataireId);

        ChatVocalDTO message = femmeRuraleService.envoyerMessageVocal(
                expediteurId, destinataireId, audioUrl
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Response.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Message vocal envoyé")
                        .data(message)
                        .build()
        );
    }

    /**
     * Envoyer un message TEXTE dans une coopérative
     * POST /api/femmes-rurales/{femmeId}/cooperatives/{cooperativeId}/messages-textes
     */
    @PostMapping("/{femmeId}/cooperatives/{cooperativeId}/messages-textes")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> envoyerMessageTexteCooperative(
            @PathVariable Long femmeId,
            @PathVariable Long cooperativeId,
            @RequestParam String texte) {

        ChatVocalDTO message = femmeRuraleService.envoyerMessageTexteCooperative(
                femmeId, cooperativeId, texte
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Response.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Message texte envoyé dans la coopérative")
                        .data(message)
                        .build()
        );
    }


    /**
     * Récupérer l'historique de chat vocal
     * GET /api/femmes-rurales/{femme1Id}/chats/{femme2Id}
     */
    @GetMapping("/{femme1Id}/chats/{femme2Id}")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> getHistoriqueChatVocal(
            @PathVariable Long femme1Id,
            @PathVariable Long femme2Id) {

        logger.info("[API] Historique de chat vocal entre " + femme1Id + " et " + femme2Id);

        List<ChatVocalDTO> historique = femmeRuraleService.getHistoriqueChatVocal(femme1Id, femme2Id);

        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Historique de chat récupéré")
                        .data(historique)
                        .build()
        );
    }

    /**
     * Marquer un message comme lu
     * PUT /api/femmes-rurales/chats/{messageId}/lu
     */
    @PutMapping("/chats/{messageId}/lu")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> marquerMessageCommeLu(@PathVariable Long messageId) {
        logger.info("[API] Marquage du message " + messageId + " comme lu");

        femmeRuraleService.marquerMessageCommeLu(messageId);

        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Message marqué comme lu")
                        .build()
        );
    }

    //================== GESTION DES COOPÉRATIVES ==================

    /**
     * Créer une coopérative
     * POST /api/femmes-rurales/{femmeId}/cooperatives
     */
    @PostMapping("/{femmeId}/cooperatives")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> creerCooperative(
            @PathVariable Long femmeId,
            @RequestParam String nom,
            @RequestParam(required = false) String description) {

        logger.info("[API] Femme " + femmeId + " crée une coopérative: " + nom);

        CoperativeDTO cooperative = femmeRuraleService.creerCooperative(femmeId, nom, description);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Response.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Coopérative créée avec succès")
                        .data(cooperative)
                        .build()
        );
    }

    /**
     * Joindre une coopérative
     * POST /api/femmes-rurales/{femmeId}/cooperatives/{cooperativeId}/joindre
     */
    @PostMapping("/{femmeId}/cooperatives/{cooperativeId}/joindre")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> joindreCooperative(
            @PathVariable Long femmeId,
            @PathVariable Long cooperativeId) {

        logger.info("[API] Femme " + femmeId + " rejoint la coopérative " + cooperativeId);

        AppartenanceDTO appartenance = femmeRuraleService.joindreCooperative(femmeId, cooperativeId);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Response.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Coopérative rejointe avec succès")
                        .data(appartenance)
                        .build()
        );
    }

    /**
     * Lister mes coopératives
     * GET /api/femmes-rurales/{femmeId}/mes-cooperatives
     */
    @GetMapping("/{femmeId}/mes-cooperatives")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> listerMesCooperatives(@PathVariable Long femmeId) {
        logger.info("[API] Femme " + femmeId + " consulte ses coopératives");

        List<CoperativeDTO> cooperatives = femmeRuraleService.listerMesCooperatives(femmeId);

        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Mes coopératives récupérées")
                        .data(cooperatives)
                        .build()
        );
    }

    /**
     * Envoyer un message vocal dans une coopérative
     * POST /api/femmes-rurales/{femmeId}/cooperatives/{cooperativeId}/messages
     */
    @PostMapping("/{femmeId}/cooperatives/{cooperativeId}/messages")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> envoyerMessageCooperative(
            @PathVariable Long femmeId,
            @PathVariable Long cooperativeId,
            @RequestParam String audioUrl) {

        logger.info("[API] Femme " + femmeId + " envoie un message dans la coopérative " + cooperativeId);

        ChatVocalDTO message = femmeRuraleService.envoyerMessageVocalCooperative(
                femmeId, cooperativeId, audioUrl
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Response.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Message envoyé dans la coopérative")
                        .data(message)
                        .build()
        );
    }

    /**
     * Récupérer les messages d'une coopérative
     * GET /api/femmes-rurales/cooperatives/{cooperativeId}/messages
     */
    @GetMapping("/cooperatives/{cooperativeId}/messages")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> getMessagesCooperative(@PathVariable Long cooperativeId) {
        logger.info("[API] Récupération des messages de la coopérative " + cooperativeId);

        List<ChatVocalDTO> messages = femmeRuraleService.getMessagesCooperative(cooperativeId);

        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(messages.size() + " message(s) trouvé(s)")
                        .data(messages)
                        .build()
        );
    }

    /**
     * Compter les messages non lus
     * GET /api/femmes-rurales/{femmeId}/messages/non-lus
     */
    @GetMapping("/{femmeId}/messages/non-lus")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> compterMessagesNonLus(@PathVariable Long femmeId) {
        logger.info("[API] Comptage des messages non lus pour la femme " + femmeId);

        Long count = femmeRuraleService.compterMessagesNonLus(femmeId);

        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Messages non lus")
                        .data(count)
                        .build()
        );
    }

    /**
     * Upload d'un audio de coopérative (fichier)
     * POST /api/femmes-rurales/{femmeId}/cooperatives/{cooperativeId}/audios
     * form-data: file = <multipart audio>
     */
    @PostMapping(
            value = "/{femmeId}/cooperatives/{cooperativeId}/audios",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> uploadAudioCooperative(
            @PathVariable Long femmeId,
            @PathVariable Long cooperativeId,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        logger.info("[API] Upload audio de coopérative " + cooperativeId + " par femme " + femmeId);

        // on pourrait vérifier ici que la femme est bien membre de la coopérative, si tu veux
        String audioUrl = femmeRuraleService.sauvegarderAudioCooperative(femmeId, file);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Response.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Audio de coopérative uploadé avec succès")
                        .data(audioUrl) // côté Flutter : json['data'] = "/uploads/audios/xxx.aac"
                        .build()
        );
    }

    // Dans FemmeRuraleController

    /**
     * Membres d'une coopérative
     * GET /api/femmes-rurales/cooperatives/{cooperativeId}/membres
     */
    @GetMapping("/cooperatives/{cooperativeId}/membres")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> getMembresCooperative(@PathVariable Long cooperativeId) {

        List<FemmeRuraleDTO> membres = femmeRuraleService.getMembresCooperative(cooperativeId);

        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(membres.size() + " membre(s) trouvé(s)")
                        .data(membres)
                        .build()
        );
    }

    /**
     * Contacts MussoDèmè à ajouter (non membres)
     * GET /api/femmes-rurales/cooperatives/{cooperativeId}/contacts-ajout
     */
    @GetMapping("/cooperatives/{cooperativeId}/contacts-ajout")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> getContactsAjoutables(@PathVariable Long cooperativeId) {

        List<FemmeRuraleDTO> contacts = femmeRuraleService.getContactsAjoutables(cooperativeId);

        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(contacts.size() + " contact(s) ajoutable(s)")
                        .data(contacts)
                        .build()
        );
    }




    //================== COMMANDES ET PAIEMENTS ==================

    /**
     * Passer une commande
     * POST /api/femmes-rurales/{femmeId}/commandes
     */
    @PostMapping("/{femmeId}/commandes")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> passerCommande(
            @PathVariable Long femmeId,
            @RequestParam Long produitId,
            @RequestParam Integer quantite) {

        logger.info("[API] Femme " + femmeId + " passe une commande (produit: " + produitId + ", quantité: " + quantite + ")");

        CommandeDTO commande = femmeRuraleService.passerCommande(femmeId, produitId, quantite);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Response.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Commande passée avec succès")
                        .data(commande)
                        .build()
        );
    }

    /**
     * Payer une commande (Orange Money, Moov Money, ou Espèce)
     * POST /api/femmes-rurales/{femmeId}/commandes/{commandeId}/payer
     */
    @PostMapping("/{femmeId}/commandes/{commandeId}/payer")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> payerCommande(
            @PathVariable Long femmeId,
            @PathVariable Long commandeId,
            @RequestParam Double montant,
            @RequestParam ModePaiement modePaiement) {

        logger.info("[API] Femme " + femmeId + " paie la commande " + commandeId + " (montant: " + montant + ", mode: " + modePaiement + ")");

        // Vérifier que le montant correspond au montant de la commande
        CommandeDTO commande = femmeRuraleService.getCommandesUtilisateur(femmeId).stream()
                .filter(c -> c.getId().equals(commandeId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Commande non trouvée"));

        // Laisser le service vérifier le montant au moment du paiement
        PaiementDTO paiement = femmeRuraleService.effectuerPaiement(commandeId, modePaiement);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Response.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Paiement " + modePaiement + " effectué avec succès")
                        .data(paiement)
                        .build()
        );
    }

    /**
     * Voir mes commandes
     * GET /api/femmes-rurales/{femmeId}/mes-commandes
     */
    @GetMapping("/{femmeId}/mes-commandes")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> voirMesCommandes(@PathVariable Long femmeId) {
        logger.info("[API] Femme " + femmeId + " consulte ses commandes");

        List<CommandeDTO> commandes = femmeRuraleService.voirMesCommandes(femmeId);

        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Mes commandes récupérées")
                        .data(commandes)
                        .build()
        );
    }

    /**
     * Voir mes ventes (commandes où la femme est vendeuse)
     * GET /api/femmes-rurales/{femmeId}/mes-ventes
     */
    @GetMapping("/{femmeId}/mes-ventes")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> voirMesVentes(@PathVariable Long femmeId) {
        logger.info("[API] Femme " + femmeId + " consulte ses ventes");

        List<CommandeDTO> ventes = femmeRuraleService.getCommandesCommeVendeur(femmeId);

        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Mes ventes récupérées")
                        .data(ventes)
                        .build()
        );
    }


    //================== PARTAGE DE CONTENUS DANS COOPÉRATIVES ==================

    /**
     * Partager un contenu (audio/vidéo) dans une coopérative
     * POST /api/femmes-rurales/{femmeId}/cooperatives/{cooperativeId}/partager
     */
    @PostMapping("/{femmeId}/cooperatives/{cooperativeId}/partager")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> partagerContenu(
            @PathVariable Long femmeId,
            @PathVariable Long cooperativeId,
            @RequestParam Long contenuId,
            @RequestParam(required = false) String messageAudioUrl) {

        logger.info("[API] Femme " + femmeId + " partage le contenu " + contenuId + " dans la coopérative " + cooperativeId);

        PartageCooperativeDTO partage = femmeRuraleService.partagerContenuDansCooperative(
                femmeId, cooperativeId, contenuId, messageAudioUrl
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Response.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Contenu partagé avec succès dans la coopérative")
                        .data(partage)
                        .build()
        );
    }

    /**
     * Récupérer les contenus partagés dans une coopérative
     * GET /api/femmes-rurales/cooperatives/{cooperativeId}/partages
     */
    @GetMapping("/cooperatives/{cooperativeId}/partages")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> getContenusPartages(@PathVariable Long cooperativeId) {
        logger.info("[API] Récupération des contenus partagés dans la coopérative " + cooperativeId);

        List<PartageCooperativeDTO> partages = femmeRuraleService.getContenusPartagesDansCooperative(cooperativeId);

        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(partages.size() + " contenu(s) partagé(s) trouvé(s)")
                        .data(partages)
                        .build()
        );
    }

    /**
     * Récupérer les contenus partagés filtrés par TypeInfo
     * GET /api/femmes-rurales/cooperatives/{cooperativeId}/partages/type/{typeInfo}
     */
    @GetMapping("/cooperatives/{cooperativeId}/partages/type/{typeInfo}")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> getContenusPartagesParType(
            @PathVariable Long cooperativeId,
            @PathVariable String typeInfo) {

        logger.info("[API] Récupération des contenus partagés (type: " + typeInfo + ") dans la coopérative " + cooperativeId);

        List<PartageCooperativeDTO> partages = femmeRuraleService.getContenusPartagesParType(
                cooperativeId, typeInfo
        );

        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(partages.size() + " contenu(s) de type " + typeInfo + " trouvé(s)")
                        .data(partages)
                        .build()
        );
    }

    /**
     * Récupérer mes partages
     * GET /api/femmes-rurales/{femmeId}/mes-partages
     */
    @GetMapping("/{femmeId}/mes-partages")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> getMesPartages(@PathVariable Long femmeId) {
        logger.info("[API] Femme " + femmeId + " consulte ses partages");

        List<PartageCooperativeDTO> partages = femmeRuraleService.getMesPartages(femmeId);

        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Mes partages récupérés")
                        .data(partages)
                        .build()
        );
    }

    /**
     * Supprimer un partage
     * DELETE /api/femmes-rurales/{femmeId}/partages/{partageId}
     */
    @DeleteMapping("/{femmeId}/partages/{partageId}")
    @PreAuthorize("hasRole('FEMME_RURALE')")
    public ResponseEntity<Response> supprimerPartage(
            @PathVariable Long femmeId,
            @PathVariable Long partageId) {

        logger.info("[API] Femme " + femmeId + " supprime le partage " + partageId);

        femmeRuraleService.supprimerPartage(femmeId, partageId);

        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Partage supprimé avec succès")
                        .build()
        );
    }
}
