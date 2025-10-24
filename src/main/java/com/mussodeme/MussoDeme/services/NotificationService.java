package com.mussodeme.MussoDeme.services;

import com.mussodeme.MussoDeme.dto.NotificationDTO;
import com.mussodeme.MussoDeme.entities.*;
import com.mussodeme.MussoDeme.enums.TypeNotif;
import com.mussodeme.MussoDeme.repository.NotificationRepository;
import com.mussodeme.MussoDeme.services.SMSService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private static final Logger logger = Logger.getLogger(NotificationService.class.getName());

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;
    private final SMSService smsService;
    private final ModelMapper modelMapper;

    // Constructor for dependency injection
    public NotificationService(NotificationRepository notificationRepository,
                              EmailService emailService,
                              SMSService smsService,
                              ModelMapper modelMapper) {
        this.notificationRepository = notificationRepository;
        this.emailService = emailService;
        this.smsService = smsService;
        this.modelMapper = modelMapper;
    }

    /**
     * Créer une notification in-app
     */
    @Transactional
    public NotificationDTO creerNotification(Utilisateur utilisateur, TypeNotif typeNotif, String description) {
        Notification notification = new Notification();
        notification.setUtilisateur(utilisateur);
        notification.setTypeNotif(typeNotif);
        notification.setDescription(description);
        notification.setStatus(false); // Non lue par défaut
        notification.setDateNotif(new Date());

        notification = notificationRepository.save(notification);
        logger.info("Notification créée pour l'utilisateur " + utilisateur.getNom() + " : " + description);

        return mapToDTO(notification);
    }

    /**
     * Notification pour nouvelle commande
     */
    @Transactional
    public void notifierNouvelleCommande(Commande commande) {
        Utilisateur vendeur = commande.getProduit().getVendeur();
        Utilisateur acheteur = commande.getAcheteur();

        String description = String.format(
                "Nouvelle commande de %s pour le produit '%s' (Quantité: %d, Montant: %s FCFA)",
                acheteur.getNom(),
                commande.getProduit().getNom(),
                commande.getQuantite(),
                commande.getMontantTotal()
        );

        // Créer notification in-app pour le vendeur
        creerNotification(vendeur, TypeNotif.COMMANDE, description);

        // Envoyer notification par le bon canal
        if (vendeur.getEmail() != null && !vendeur.getEmail().isEmpty()) {
            // Utilisateur avec email (admin) -> email
            emailService.envoyerEmailNouvelleCommande(commande, vendeur);
        } else {
            // Femme rurale -> SMS
            smsService.envoyerSMSNouvelleCommande(commande);
        }

        logger.info("Notification de nouvelle commande envoyée au vendeur " + vendeur.getNom());
    }

    /**
     * Notification pour paiement reçu
     */
    @Transactional
    public void notifierPaiementRecu(Paiement paiement) {
        Utilisateur vendeur = paiement.getCommande().getProduit().getVendeur();
        Utilisateur acheteur = paiement.getAcheteur();

        String description = String.format(
                "Paiement de %s FCFA reçu de %s via %s pour le produit '%s'",
                paiement.getMontant(),
                acheteur.getNom(),
                paiement.getModePaiement(),
                paiement.getCommande().getProduit().getNom()
        );

        // Créer notification in-app pour le vendeur
        creerNotification(vendeur, TypeNotif.PAIEMENT, description);

        // Envoyer notification par le bon canal
        if (vendeur.getEmail() != null && !vendeur.getEmail().isEmpty()) {
            // Utilisateur avec email (admin) -> email
            emailService.envoyerEmailPaiementRecu(paiement, vendeur);
        } else {
            // Femme rurale -> SMS
            smsService.envoyerSMSPaiementRecu(paiement);
        }

        logger.info("Notification de paiement reçu envoyée au vendeur " + vendeur.getNom());
    }

    /**
     * Notification pour nouvelle publication de produit
     */
    @Transactional
    public void notifierNouveauProduit(Produit produit, List<Utilisateur> destinataires) {
        String description = String.format(
                "Nouveau produit publié : '%s' par %s (Prix: %s FCFA, Stock: %d)",
                produit.getNom(),
                produit.getVendeur().getNom(),
                produit.getPrix(),
                produit.getStock()
        );

        // Créer notification et envoyer par le bon canal à chaque destinataire
        for (Utilisateur destinataire : destinataires) {
            creerNotification(destinataire, TypeNotif.PUBLICATION, description);
            
            if (destinataire.getEmail() != null && !destinataire.getEmail().isEmpty()) {
                // Utilisateur avec email (admin) -> email
                emailService.envoyerEmailNouveauProduit(produit, destinataire);
            } else {
                // Femme rurale -> SMS
                smsService.envoyerSMSNouveauProduit(produit, destinataire);
            }
        }

        logger.info("Notification de nouveau produit envoyée à " + destinataires.size() + " utilisateurs");
    }

    /**
     * Notification pour nouveau membre dans coopérative
     */
    @Transactional
    public void notifierNouveauMembreCooperative(Coperative cooperative, FemmeRurale nouveauMembre, List<Utilisateur> membres) {
        String description = String.format(
                "%s a rejoint la coopérative '%s'",
                nouveauMembre.getNom(),
                cooperative.getNom()
        );

        // Notifier tous les membres existants
        for (Utilisateur membre : membres) {
            if (!membre.getId().equals(nouveauMembre.getId())) { // Ne pas notifier le nouveau membre lui-même
                creerNotification(membre, TypeNotif.COOPERATIVE, description);
                
                if (membre.getEmail() != null && !membre.getEmail().isEmpty()) {
                    // Utilisateur avec email (admin) -> email
                    emailService.envoyerEmailNouveauMembreCooperative(cooperative, nouveauMembre, membre);
                } else {
                    // Femme rurale -> SMS
                    smsService.envoyerSMSNouveauMembre(cooperative, nouveauMembre, membre);
                }
            }
        }

        logger.info("Notification de nouveau membre envoyée à " + membres.size() + " membres de la coopérative " + cooperative.getNom());
    }

    /**
     * Notification pour contenu partagé dans coopérative
     */
    @Transactional
    public void notifierContenuPartage(Contenu contenu, Coperative cooperative, List<Utilisateur> membres) {
        String description = String.format(
                "Nouveau contenu partagé dans '%s' : %s (%s)",
                cooperative.getNom(),
                contenu.getTitre(),
                contenu.getTypeContenu()
        );

        // Notifier tous les membres de la coopérative
        for (Utilisateur membre : membres) {
            creerNotification(membre, TypeNotif.CONTENU_PARTAGE, description);
            
            if (membre.getEmail() != null && !membre.getEmail().isEmpty()) {
                // Utilisateur avec email (admin) -> email
                emailService.envoyerEmailContenuPartage(contenu, cooperative, membre);
            } else {
                // Femme rurale -> SMS
                smsService.envoyerSMSContenuPartage(contenu, cooperative, membre);
            }
        }

        logger.info("Notification de contenu partagé envoyée à " + membres.size() + " membres de la coopérative " + cooperative.getNom());
    }

    /**
     * Notification pour changement de statut de commande
     */
    @Transactional
    public void notifierChangementStatutCommande(Commande commande) {
        Utilisateur acheteur = commande.getAcheteur();
        
        String description = String.format(
                "Le statut de votre commande #%d pour le produit '%s' a été mis à jour : %s",
                commande.getId(),
                commande.getProduit().getNom(),
                commande.getStatutCommande().name() // Using name() instead of getDisplayName()
        );
        
        // Créer notification in-app pour l'acheteur
        creerNotification(acheteur, TypeNotif.COMMANDE, description);
        
        // Envoyer notification par le bon canal
        if (acheteur.getEmail() != null && !acheteur.getEmail().isEmpty()) {
            // Utilisateur avec email (admin) -> email
            // Using existing method instead of non-existent envoyerEmailChangementStatutCommande
            emailService.envoyerEmailNouvelleCommande(commande, acheteur);
        } else {
            // Femme rurale -> SMS
            // Using existing method instead of non-existent envoyerSMSChangementStatutCommande
            smsService.envoyerSMSNouvelleCommande(commande);
        }
        
        logger.info("Notification de changement de statut de commande envoyée à l'acheteur " + acheteur.getNom());
    }

    /**
     * Récupérer toutes les notifications d'un utilisateur
     */
    @Transactional(readOnly = true)
    public List<NotificationDTO> getNotificationsUtilisateur(Long utilisateurId) {
        // Instead of instantiating abstract Utilisateur, we'll fetch by ID directly
        List<Notification> notifications = notificationRepository.findByUtilisateurId(utilisateurId);
        return notifications.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer les notifications non lues d'un utilisateur
     */
    @Transactional(readOnly = true)
    public List<NotificationDTO> getNotificationsNonLues(Long utilisateurId) {
        // Instead of instantiating abstract Utilisateur, we'll fetch by ID directly
        List<Notification> notifications = notificationRepository.findByUtilisateurIdAndStatusFalse(utilisateurId);
        return notifications.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Marquer une notification comme lue
     */
    @Transactional
    public NotificationDTO marquerCommeLue(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification non trouvée avec l'ID : " + notificationId));

        notification.setStatus(true);
        notification = notificationRepository.save(notification);

        logger.info("Notification " + notificationId + " marquée comme lue");
        return mapToDTO(notification);
    }

    /**
     * Marquer toutes les notifications d'un utilisateur comme lues
     */
    @Transactional
    public void marquerToutesCommeLues(Long utilisateurId) {
        // Instead of instantiating abstract Utilisateur, we'll update by ID directly
        notificationRepository.markAllAsReadForUser(utilisateurId);
        logger.info("Toutes les notifications marquées comme lues pour l'utilisateur " + utilisateurId);
    }

    /**
     * Compter les notifications non lues d'un utilisateur
     */
    @Transactional(readOnly = true)
    public Long compterNotificationsNonLues(Long utilisateurId) {
        return notificationRepository.countUnreadNotificationsByUserId(utilisateurId);
    }

    /**
     * Récupérer les notifications d'un utilisateur par type
     */
    @Transactional(readOnly = true)
    public List<NotificationDTO> getNotificationsParType(Long utilisateurId, TypeNotif typeNotif) {
        // Instead of instantiating abstract Utilisateur, we'll fetch by ID and type directly
        List<Notification> notifications = notificationRepository.findByUtilisateurIdAndTypeNotif(utilisateurId, typeNotif);
        return notifications.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Supprimer une notification
     */
    @Transactional
    public void supprimerNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
        logger.info("Notification " + notificationId + " supprimée");
    }

    // ==================== MAPPING ====================

    private NotificationDTO mapToDTO(Notification notification) {
        NotificationDTO dto = modelMapper.map(notification, NotificationDTO.class);
        dto.setUtilisateurId(notification.getUtilisateur().getId());
        dto.setUtilisateurNom(notification.getUtilisateur().getNom());
        return dto;
    }
}