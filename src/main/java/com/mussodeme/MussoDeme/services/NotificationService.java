package com.mussodeme.MussoDeme.services;

import com.mussodeme.MussoDeme.dto.NotificationDTO;
import com.mussodeme.MussoDeme.dto.NotifAdminDTO;
import com.mussodeme.MussoDeme.entities.*;
import com.mussodeme.MussoDeme.enums.TypeNotif;
import com.mussodeme.MussoDeme.repository.NotificationRepository;
import com.mussodeme.MussoDeme.repository.NotifAdminRepository;
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
    private final NotifAdminRepository notifAdminRepository;
    private final EmailService emailService;
    private final SMSService smsService;
    private final ModelMapper modelMapper;

    // Constructor for dependency injection
    public NotificationService(NotificationRepository notificationRepository,
                              NotifAdminRepository notifAdminRepository,
                              EmailService emailService,
                              SMSService smsService,
                              ModelMapper modelMapper) {
        this.notificationRepository = notificationRepository;
        this.notifAdminRepository = notifAdminRepository;
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
        
        // Check if utilisateur is a transient instance (not yet saved)
        if (utilisateur.getId() == null) {
            // If utilisateur is transient, we can't create a notification for it
            // Log the issue and return null or throw an exception
            logger.warning("Impossible de créer une notification pour un utilisateur non enregistré : " + description);
            return null;
        }
        
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
     * Créer une notification in-app pour un administrateur
     */
    @Transactional
    public NotifAdminDTO creerNotificationAdmin(Admin admin, TypeNotif typeNotif, String description) {
        NotifAdmin notification = new NotifAdmin();
        
        // Check if admin is a transient instance (not yet saved)
        if (admin.getId() == null) {
            // If admin is transient, we can't create a notification for it
            // Log the issue and return null or throw an exception
            logger.warning("Impossible de créer une notification pour un admin non enregistré : " + description);
            return null;
        }
        
        notification.setAdmin(admin);
        notification.setTypeNotif(typeNotif);
        notification.setDescription(description);
        notification.setStatus(false); // Non lue par défaut
        notification.setDateNotif(new Date());

        notification = notifAdminRepository.save(notification);
        logger.info("Notification créée pour l'admin " + admin.getNom() + " : " + description);

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
        NotificationDTO notificationVendeur = creerNotification(vendeur, TypeNotif.COMMANDE, description);
        
        // Only send external notification if internal notification was created successfully
        if (notificationVendeur != null) {
            // Envoyer notification par le bon canal
            if (vendeur.getEmail() != null && !vendeur.getEmail().isEmpty()) {
                // Utilisateur avec email (admin) -> email
                try {
                    emailService.envoyerEmailNouvelleCommande(commande, vendeur);
                } catch (Exception e) {
                    logger.severe("Erreur lors de l'envoi de l'email au vendeur : " + e.getMessage());
                }
            } else {
                // Femme rurale -> SMS
                try {
                    smsService.envoyerSMSNouvelleCommande(commande);
                } catch (Exception e) {
                    logger.severe("Erreur lors de l'envoi du SMS au vendeur : " + e.getMessage());
                }
            }
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
        NotificationDTO notificationVendeur = creerNotification(vendeur, TypeNotif.PAIEMENT, description);
        
        // Only send external notification if internal notification was created successfully
        if (notificationVendeur != null) {
            // Envoyer notification par le bon canal
            if (vendeur.getEmail() != null && !vendeur.getEmail().isEmpty()) {
                // Utilisateur avec email (admin) -> email
                try {
                    emailService.envoyerEmailPaiementRecu(paiement, vendeur);
                } catch (Exception e) {
                    logger.severe("Erreur lors de l'envoi de l'email au vendeur : " + e.getMessage());
                }
            } else {
                // Femme rurale -> SMS
                try {
                    smsService.envoyerSMSPaiementRecu(paiement);
                } catch (Exception e) {
                    logger.severe("Erreur lors de l'envoi du SMS au vendeur : " + e.getMessage());
                }
            }
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

        int successfulNotifications = 0;
        
        // Créer notification et envoyer par le bon canal à chaque destinataire
        for (Utilisateur destinataire : destinataires) {
            NotificationDTO notification = creerNotification(destinataire, TypeNotif.PUBLICATION, description);
            
            // Only send external notification if internal notification was created successfully
            if (notification != null) {
                if (destinataire.getEmail() != null && !destinataire.getEmail().isEmpty()) {
                    // Utilisateur avec email (admin) -> email
                    try {
                        emailService.envoyerEmailNouveauProduit(produit, destinataire);
                    } catch (Exception e) {
                        logger.severe("Erreur lors de l'envoi de l'email au destinataire " + destinataire.getNom() + ": " + e.getMessage());
                    }
                } else {
                    // Femme rurale -> SMS
                    try {
                        smsService.envoyerSMSNouveauProduit(produit, destinataire);
                    } catch (Exception e) {
                        logger.severe("Erreur lors de l'envoi du SMS au destinataire " + destinataire.getNom() + ": " + e.getMessage());
                    }
                }
                successfulNotifications++;
            }
        }

        logger.info("Notification de nouveau produit envoyée à " + successfulNotifications + " utilisateurs sur " + destinataires.size());
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

        int successfulNotifications = 0;
        
        // Notifier tous les membres existants
        for (Utilisateur membre : membres) {
            if (!membre.getId().equals(nouveauMembre.getId())) { // Ne pas notifier le nouveau membre lui-même
                NotificationDTO notification = creerNotification(membre, TypeNotif.COOPERATIVE, description);
                
                // Only send external notification if internal notification was created successfully
                if (notification != null) {
                    if (membre.getEmail() != null && !membre.getEmail().isEmpty()) {
                        // Utilisateur avec email (admin) -> email
                        try {
                            emailService.envoyerEmailNouveauMembreCooperative(cooperative, nouveauMembre, membre);
                        } catch (Exception e) {
                            logger.severe("Erreur lors de l'envoi de l'email au membre " + membre.getNom() + ": " + e.getMessage());
                        }
                    } else {
                        // Femme rurale -> SMS
                        try {
                            smsService.envoyerSMSNouveauMembre(cooperative, nouveauMembre, membre);
                        } catch (Exception e) {
                            logger.severe("Erreur lors de l'envoi du SMS au membre " + membre.getNom() + ": " + e.getMessage());
                        }
                    }
                    successfulNotifications++;
                }
            }
        }

        logger.info("Notification de nouveau membre envoyée à " + successfulNotifications + " membres de la coopérative " + cooperative.getNom());
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

        int successfulNotifications = 0;
        
        // Notifier tous les membres de la coopérative
        for (Utilisateur membre : membres) {
            NotificationDTO notification = creerNotification(membre, TypeNotif.CONTENU_PARTAGE, description);
            
            // Only send external notification if internal notification was created successfully
            if (notification != null) {
                if (membre.getEmail() != null && !membre.getEmail().isEmpty()) {
                    // Utilisateur avec email (admin) -> email
                    try {
                        emailService.envoyerEmailContenuPartage(contenu, cooperative, membre);
                    } catch (Exception e) {
                        logger.severe("Erreur lors de l'envoi de l'email au membre " + membre.getNom() + ": " + e.getMessage());
                    }
                } else {
                    // Femme rurale -> SMS
                    try {
                        smsService.envoyerSMSContenuPartage(contenu, cooperative, membre);
                    } catch (Exception e) {
                        logger.severe("Erreur lors de l'envoi du SMS au membre " + membre.getNom() + ": " + e.getMessage());
                    }
                }
                successfulNotifications++;
            }
        }

        logger.info("Notification de contenu partagé envoyée à " + successfulNotifications + " membres de la coopérative " + cooperative.getNom());
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
        NotificationDTO notificationAcheteur = creerNotification(acheteur, TypeNotif.COMMANDE, description);
        
        // Only send external notification if internal notification was created successfully
        if (notificationAcheteur != null) {
            // Envoyer notification par le bon canal
            if (acheteur.getEmail() != null && !acheteur.getEmail().isEmpty()) {
                // Utilisateur avec email (admin) -> email
                // Using existing method instead of non-existent envoyerEmailChangementStatutCommande
                try {
                    emailService.envoyerEmailNouvelleCommande(commande, acheteur);
                } catch (Exception e) {
                    logger.severe("Erreur lors de l'envoi de l'email à l'acheteur : " + e.getMessage());
                }
            } else {
                // Femme rurale -> SMS
                // Using existing method instead of non-existent envoyerSMSChangementStatutCommande
                try {
                    smsService.envoyerSMSNouvelleCommande(commande);
                } catch (Exception e) {
                    logger.severe("Erreur lors de l'envoi du SMS à l'acheteur : " + e.getMessage());
                }
            }
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

    /**
     * Récupérer toutes les notifications d'un admin
     */
    @Transactional(readOnly = true)
    public List<NotifAdminDTO> getNotificationsAdmin(Long adminId) {
        List<NotifAdmin> notifications = notifAdminRepository.findByAdminId(adminId);
        return notifications.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer les notifications non lues d'un admin
     */
    @Transactional(readOnly = true)
    public List<NotifAdminDTO> getNotificationsAdminNonLues(Long adminId) {
        List<NotifAdmin> notifications = notifAdminRepository.findByAdminIdAndStatusFalse(adminId);
        return notifications.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Marquer une notification admin comme lue
     */
    @Transactional
    public NotifAdminDTO marquerNotificationAdminCommeLue(Long notificationId) {
        NotifAdmin notification = notifAdminRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification admin non trouvée avec l'ID : " + notificationId));

        notification.setStatus(true);
        notification = notifAdminRepository.save(notification);

        logger.info("Notification admin " + notificationId + " marquée comme lue");
        return mapToDTO(notification);
    }

    /**
     * Marquer toutes les notifications d'un admin comme lues
     */
    @Transactional
    public void marquerToutesNotificationsAdminCommeLues(Long adminId) {
        notifAdminRepository.markAllAsReadForAdmin(adminId);
        logger.info("Toutes les notifications marquées comme lues pour l'admin " + adminId);
    }

    /**
     * Compter les notifications non lues d'un admin
     */
    @Transactional(readOnly = true)
    public Long compterNotificationsAdminNonLues(Long adminId) {
        return notifAdminRepository.countUnreadNotificationsByAdminId(adminId);
    }

    /**
     * Récupérer les notifications d'un admin par type
     */
    @Transactional(readOnly = true)
    public List<NotifAdminDTO> getNotificationsAdminParType(Long adminId, TypeNotif typeNotif) {
        List<NotifAdmin> notifications = notifAdminRepository.findByAdminIdAndTypeNotif(adminId, typeNotif);
        return notifications.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Supprimer une notification admin
     */
    @Transactional
    public void supprimerNotificationAdmin(Long notificationId) {
        notifAdminRepository.deleteById(notificationId);
        logger.info("Notification admin " + notificationId + " supprimée");
    }

    // ==================== MAPPING ====================

    private NotificationDTO mapToDTO(Notification notification) {
        NotificationDTO dto = modelMapper.map(notification, NotificationDTO.class);
        dto.setUtilisateurId(notification.getUtilisateur().getId());
        dto.setUtilisateurNom(notification.getUtilisateur().getNom());
        return dto;
    }
    
    private NotifAdminDTO mapToDTO(NotifAdmin notification) {
        NotifAdminDTO dto = modelMapper.map(notification, NotifAdminDTO.class);
        dto.setAdminId(notification.getAdmin().getId());
        dto.setAdminNom(notification.getAdmin().getNom());
        return dto;
    }
}