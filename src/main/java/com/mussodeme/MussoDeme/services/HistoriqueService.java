package com.mussodeme.MussoDeme.services;

import com.mussodeme.MussoDeme.dto.HistoriqueDTO;
import com.mussodeme.MussoDeme.entities.*;
import com.mussodeme.MussoDeme.enums.TypeHistoriques;
import com.mussodeme.MussoDeme.exceptions.NotFoundException;
import com.mussodeme.MussoDeme.repository.HistoriqueRepository;
import com.mussodeme.MussoDeme.repository.UtilisateursRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Logger;

/**
 * Service pour gérer les historiques de toutes les actions dans l'application
 * Trace : ventes, achats, téléchargements, paiements
 */
@Service
public class HistoriqueService {

    private static final Logger logger = Logger.getLogger(HistoriqueService.class.getName());

    private final HistoriqueRepository historiqueRepository;
    private final UtilisateursRepository utilisateursRepository;

    // Constructor for dependency injection
    public HistoriqueService(HistoriqueRepository historiqueRepository, UtilisateursRepository utilisateursRepository) {
        this.historiqueRepository = historiqueRepository;
        this.utilisateursRepository = utilisateursRepository;
    }

    //================== ENREGISTREMENT AUTOMATIQUE DES HISTORIQUES ==================

    /**
     * Enregistrer une vente de produit
     */
    @Transactional
    public void enregistrerVente(Utilisateur vendeur, Produit produit, Double montant, Integer quantite) {
        logger.info("Enregistrement vente : utilisateur=" + vendeur.getId() + ", produit=" + produit.getId() + ", montant=" + montant);
        
        String description = String.format("Vente de %d x %s - %,.0f FCFA", 
                quantite, produit.getNom(), montant);
        
        Historique historique = new Historique();
        historique.setTypeHistoriques(TypeHistoriques.HISTORIQUE_DES_VENTES);
        historique.setUtilisateur(vendeur);
        historique.setEntiteId(produit.getId());
        historique.setEntiteType("Produit");
        historique.setDescription(description);
        historique.setMontant(montant);
        historique.setDateAction(LocalDateTime.now());
        
        historiqueRepository.save(historique);
        logger.fine("Historique de vente enregistré : " + historique.getId());
    }

    /**
     * Enregistrer un achat/commande
     */
    @Transactional
    public void enregistrerAchat(Utilisateur acheteur, Commande commande, Produit produit) {
        logger.info("Enregistrement achat : utilisateur=" + acheteur.getId() + ", commande=" + commande.getId());
        
        Double montantTotal = produit.getPrix() * commande.getQuantite();
        String description = String.format("Achat de %d x %s - %,.0f FCFA", 
                commande.getQuantite(), produit.getNom(), montantTotal);
        
        Historique historique = new Historique();
        historique.setTypeHistoriques(TypeHistoriques.HISTORIQUE_DES_ACHATS);
        historique.setUtilisateur(acheteur);
        historique.setEntiteId(commande.getId());
        historique.setEntiteType("Commande");
        historique.setDescription(description);
        historique.setMontant(montantTotal);
        historique.setDateAction(LocalDateTime.now());
        
        historiqueRepository.save(historique);
        logger.fine("Historique d'achat enregistré : " + historique.getId());
    }

    /**
     * Enregistrer un téléchargement/lecture de contenu
     */
    @Transactional
    public void enregistrerTelechargement(Utilisateur utilisateur, Contenu contenu) {
        logger.info("Enregistrement téléchargement : utilisateur=" + utilisateur.getId() + ", contenu=" + contenu.getId());
        
        String typeContenu = contenu.getTypeInfo() != null ? contenu.getTypeInfo().toString() : "Contenu";
        String description = String.format("Téléchargement/Écoute : %s (%s)", 
                contenu.getTitre(), typeContenu);
        
        Historique historique = new Historique();
        historique.setTypeHistoriques(TypeHistoriques.HISTORIQUE_DES_TELECHARGEMENTS);
        historique.setUtilisateur(utilisateur);
        historique.setEntiteId(contenu.getId());
        historique.setEntiteType("Contenu");
        historique.setDescription(description);
        historique.setDateAction(LocalDateTime.now());
        
        historiqueRepository.save(historique);
        logger.fine("Historique de téléchargement enregistré : " + historique.getId());
    }

    /**
     * Enregistrer un paiement
     */
    @Transactional
    public void enregistrerPaiement(Utilisateur utilisateur, Paiement paiement, Commande commande) {
        logger.info("Enregistrement paiement : utilisateur=" + utilisateur.getId() + ", paiement=" + paiement.getId());
        
        String description = String.format("Paiement %s - %,.0f FCFA (Commande #%d)", 
                paiement.getModePaiement(), paiement.getMontant(), commande.getId());
        
        Historique historique = new Historique();
        historique.setTypeHistoriques(TypeHistoriques.HISTORIQUE_DES_PAIEMENTS);
        historique.setUtilisateur(utilisateur);
        historique.setEntiteId(paiement.getId());
        historique.setEntiteType("Paiement");
        historique.setDescription(description);
        historique.setMontant(paiement.getMontant());
        historique.setDateAction(LocalDateTime.now());
        
        historiqueRepository.save(historique);
        logger.fine("Historique de paiement enregistré : " + historique.getId());
    }

    /**
     * Enregistrer un partage de contenu dans une coopérative
     */
    @Transactional
    public void enregistrerPartage(Utilisateur utilisateur, Contenu contenu, Coperative cooperative) {
        logger.info("Enregistrement partage : utilisateur=" + utilisateur.getId() + ", contenu=" + contenu.getId() + ", coopérative=" + cooperative.getId());
        
        String description = String.format("Partage de '%s' dans la coopérative '%s'", 
                contenu.getTitre(), cooperative.getNom());
        
        Historique historique = new Historique();
        historique.setTypeHistoriques(TypeHistoriques.HISTORIQUE_DES_PARTAGES);
        historique.setUtilisateur(utilisateur);
        historique.setEntiteId(contenu.getId());
        historique.setEntiteType("Partage");
        historique.setDescription(description);
        historique.setDateAction(LocalDateTime.now());
        
        historiqueRepository.save(historique);
        logger.fine("Historique de partage enregistré : " + historique.getId());
    }

    /**
     * Enregistrer une publication de produit
     */
    @Transactional
    public void enregistrerPublication(Utilisateur utilisateur, Produit produit) {
        logger.info("Enregistrement publication : utilisateur=" + utilisateur.getId() + ", produit=" + produit.getId());
        
        String typeProduit = produit.getTypeProduit() != null ? produit.getTypeProduit().toString() : "Produit";
        String description = String.format("Publication de '%s' (%s) - %,.0f FCFA (%d unités)", 
                produit.getNom(), typeProduit, produit.getPrix(), produit.getQuantite());
        
        Historique historique = new Historique();
        historique.setTypeHistoriques(TypeHistoriques.HISTORIQUE_DES_PUBLICATIONS);
        historique.setUtilisateur(utilisateur);
        historique.setEntiteId(produit.getId());
        historique.setEntiteType("Produit");
        historique.setDescription(description);
        historique.setMontant(produit.getPrix());
        historique.setDateAction(LocalDateTime.now());
        
        historiqueRepository.save(historique);
        logger.fine("Historique de publication enregistré : " + historique.getId());
    }

    /**
     * Enregistrer une modification de produit
     */
    @Transactional
    public void enregistrerModification(Utilisateur utilisateur, Produit produit, String details) {
        logger.info("Enregistrement modification produit : utilisateur=" + utilisateur.getId() + ", produit=" + produit.getId());
        
        String description = String.format("Modification de '%s' : %s", produit.getNom(), details);
        
        Historique historique = new Historique();
        historique.setTypeHistoriques(TypeHistoriques.HISTORIQUE_DES_PUBLICATIONS);
        historique.setUtilisateur(utilisateur);
        historique.setEntiteId(produit.getId());
        historique.setEntiteType("Produit");
        historique.setDescription(description);
        historique.setDateAction(LocalDateTime.now());
        
        historiqueRepository.save(historique);
        logger.fine("Historique de modification enregistré : " + historique.getId());
    }

    /**
     * Enregistrer une suppression de produit
     */
    @Transactional
    public void enregistrerSuppressionProduit(Utilisateur utilisateur, Produit produit) {
        logger.info("Enregistrement suppression produit : utilisateur=" + utilisateur.getId() + ", produit=" + produit.getId());
        
        String description = String.format("Suppression de '%s'", produit.getNom());
        
        Historique historique = new Historique();
        historique.setTypeHistoriques(TypeHistoriques.HISTORIQUE_DES_PUBLICATIONS);
        historique.setUtilisateur(utilisateur);
        historique.setEntiteId(produit.getId());
        historique.setEntiteType("Produit");
        historique.setDescription(description);
        historique.setDateAction(LocalDateTime.now());
        
        historiqueRepository.save(historique);
        logger.fine("Historique de suppression enregistré : " + historique.getId());
    }

    /**
     * Enregistrer une adhésion à une coopérative (création ou adhésion)
     */
    @Transactional
    public void enregistrerAdhesionCooperative(Utilisateur utilisateur, Coperative cooperative, boolean creation) {
        logger.info("Enregistrement adhésion coopérative : utilisateur=" + utilisateur.getId() + ", coopérative=" + cooperative.getId() + ", création=" + creation);
        
        String action = creation ? "Création" : "Adhésion";
        String description = String.format("%s de la coopérative '%s'", action, cooperative.getNom());
        
        Historique historique = new Historique();
        historique.setTypeHistoriques(TypeHistoriques.HISTORIQUE_ADHESIONS_COOPERATIVES);
        historique.setUtilisateur(utilisateur);
        historique.setEntiteId(cooperative.getId());
        historique.setEntiteType("Cooperative");
        historique.setDescription(description);
        historique.setDateAction(LocalDateTime.now());
        
        historiqueRepository.save(historique);
        logger.fine("Historique d'adhésion enregistré : " + historique.getId());
    }

    //================== CONSULTATION DES HISTORIQUES ==================

    /**
     * Récupérer tous les historiques d'un utilisateur
     */
    public List<HistoriqueDTO> getHistoriquesUtilisateur(Long utilisateurId) {
        logger.info("Récupération des historiques de l'utilisateur " + utilisateurId);
        
        Utilisateur utilisateur = utilisateursRepository.findById(utilisateurId)
                .orElseThrow(() -> new NotFoundException("Utilisateur non trouvé avec l'ID: " + utilisateurId));
        
        List<Historique> historiques = historiqueRepository.findByUtilisateur(utilisateur);
        
        return historiques.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer les historiques d'un utilisateur par type
     */
    public List<HistoriqueDTO> getHistoriquesParType(Long utilisateurId, String typeHistorique) {
        logger.info("Récupération des historiques de type " + typeHistorique + " pour l'utilisateur " + utilisateurId);
        
        try {
            TypeHistoriques type = TypeHistoriques.valueOf(typeHistorique.toUpperCase());
            
            Utilisateur utilisateur = utilisateursRepository.findById(utilisateurId)
                    .orElseThrow(() -> new NotFoundException("Utilisateur non trouvé avec l'ID: " + utilisateurId));
            
            List<Historique> historiques = historiqueRepository.findByUtilisateurAndType(utilisateur, type);
            
            return historiques.stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            logger.warning("Type d'historique invalide: " + typeHistorique);
            throw new IllegalArgumentException("Type d'historique invalide. Valeurs acceptées: " +
                    "HISTORIQUE_DES_VENTES, HISTORIQUE_DES_ACHATS, HISTORIQUE_DES_TELECHARGEMENTS, HISTORIQUE_DES_PAIEMENTS");
        }
    }

    /**
     * Récupérer les historiques entre deux dates
     */
    public List<HistoriqueDTO> getHistoriquesParPeriode(
            Long utilisateurId, 
            LocalDateTime dateDebut, 
            LocalDateTime dateFin) {
        
        logger.info("Récupération des historiques de l'utilisateur " + utilisateurId + " entre " + dateDebut + " et " + dateFin);
        
        Utilisateur utilisateur = utilisateursRepository.findById(utilisateurId)
                .orElseThrow(() -> new NotFoundException("Utilisateur non trouvé avec l'ID: " + utilisateurId));
        
        List<Historique> historiques = historiqueRepository.findByUtilisateurAndDateBetween(
                utilisateur, dateDebut, dateFin);
        
        return historiques.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer tous les historiques (pour admin)
     */
    public List<HistoriqueDTO> getTousLesHistoriques() {
        logger.info("Récupération de tous les historiques");
        
        List<Historique> historiques = historiqueRepository.findAll();
        
        return historiques.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer les historiques par type global (pour admin)
     */
    public List<HistoriqueDTO> getHistoriquesGlobauxParType(String typeHistorique) {
        logger.info("Récupération des historiques globaux de type " + typeHistorique);
        
        try {
            TypeHistoriques type = TypeHistoriques.valueOf(typeHistorique.toUpperCase());
            List<Historique> historiques = historiqueRepository.findByType(type);
            
            return historiques.stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            logger.warning("Type d'historique invalide: " + typeHistorique);
            throw new IllegalArgumentException("Type d'historique invalide");
        }
    }

    /**
     * Obtenir des statistiques par type pour un utilisateur
     */
    public Long getStatistiquesParType(Long utilisateurId, String typeHistorique) {
        logger.info("Statistiques " + typeHistorique + " pour l'utilisateur " + utilisateurId);
        
        try {
            TypeHistoriques type = TypeHistoriques.valueOf(typeHistorique.toUpperCase());
            
            Utilisateur utilisateur = utilisateursRepository.findById(utilisateurId)
                    .orElseThrow(() -> new NotFoundException("Utilisateur non trouvé avec l'ID: " + utilisateurId));
            
            return historiqueRepository.countByUtilisateurAndType(utilisateur, type);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Type d'historique invalide");
        }
    }

    //================== MAPPER ==================

    /**
     * Mapper Historique vers DTO
     */
    private HistoriqueDTO mapToDTO(Historique historique) {
        HistoriqueDTO dto = new HistoriqueDTO();
        dto.setId(historique.getId());
        dto.setTypeHistoriques(historique.getTypeHistoriques());
        dto.setDateAction(historique.getDateAction());
        dto.setUtilisateurId(historique.getUtilisateur().getId());
        dto.setUtilisateurNom(historique.getUtilisateur().getNom());
        dto.setUtilisateurPrenom(historique.getUtilisateur().getPrenom());
        dto.setEntiteId(historique.getEntiteId());
        dto.setEntiteType(historique.getEntiteType());
        dto.setDescription(historique.getDescription());
        dto.setMontant(historique.getMontant());
        return dto;
    }
}