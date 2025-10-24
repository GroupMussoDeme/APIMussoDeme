package com.mussodeme.MussoDeme.services;

import com.mussodeme.MussoDeme.entities.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@Service
public class SMSService {

    private static final Logger logger = Logger.getLogger(SMSService.class.getName());

    @Value("${sms.api.url:}")
    private String smsApiUrl;
    
    @Value("${sms.api.key:}")
    private String smsApiKey;
    
    @Value("${sms.sender.id:MussoDeme}")
    private String senderId;
    
    private final RestTemplate restTemplate;

    // Constructor for dependency injection
    public SMSService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Envoyer un SMS à un utilisateur
     * Seulement pour les femmes rurales (pas d'email)
     */
    public void envoyerSMS(Utilisateur utilisateur, String message) {
        // Vérifier que c'est une femme rurale (n'a pas d'email)
        if (utilisateur.getEmail() != null && !utilisateur.getEmail().isEmpty()) {
            logger.fine("Utilisateur " + utilisateur.getNom() + " a un email, pas d'envoi de SMS");
            return;
        }
        
        // Vérifier que l'utilisateur a un numéro de téléphone
        if (utilisateur.getNumeroTel() == null || utilisateur.getNumeroTel().isEmpty()) {
            logger.warning("Utilisateur " + utilisateur.getNom() + " n'a pas de numéro de téléphone");
            return;
        }
        
        // Vérifier que le SMS est configuré
        if (smsApiUrl == null || smsApiUrl.isEmpty() || smsApiKey == null || smsApiKey.isEmpty()) {
            logger.warning("API SMS non configurée - impossible d'envoyer le SMS à " + utilisateur.getNumeroTel());
            return;
        }
        
        try {
            // Formatage du numéro 
            String numeroFormate = formaterNumeroMali(utilisateur.getNumeroTel());
            
            // Construction du JSON pour Africa's Talking
            String jsonPayload = String.format(
                "username=%s&to=%s&from=%s&message=%s",
                "your-username", numeroFormate, senderId, message
            );
            
            // Création de l'entête HTTP pour Africa's Talking
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("apikey", smsApiKey);
            
            // Création de la requête
            HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);
            
            // Envoi du SMS via Africa's Talking
            ResponseEntity<String> response = restTemplate.postForEntity(smsApiUrl, entity, String.class);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("SMS envoyé avec succès à " + numeroFormate + ": " + message);
            } else {
                logger.severe("Erreur lors de l'envoi du SMS à " + numeroFormate + ": " + response.getStatusCode());
            }
        } catch (Exception e) {
            logger.severe("Erreur lors de l'envoi du SMS à " + utilisateur.getNumeroTel() + ": " + e.getMessage());
        }
    }
    
    /**
     * Formater le numéro pour le Mali (+223)
     */
    private String formaterNumeroMali(String numero) {
        // Enlever les espaces et caractères spéciaux
        String cleanNumero = numero.replaceAll("[\\s\\-\\(\\)]+", "");
        
        // Si le numéro commence déjà par +223, le retourner tel quel
        if (cleanNumero.startsWith("+223")) {
            return cleanNumero;
        }
        
        // Si le numéro commence par 00223, remplacer par +223
        if (cleanNumero.startsWith("00223")) {
            return cleanNumero.replaceFirst("00223", "+223");
        }
        
        // Si le numéro commence par 223, ajouter le +
        if (cleanNumero.startsWith("223")) {
            return "+" + cleanNumero;
        }
        
        // Si le numéro commence par 0, remplacer par +223
        if (cleanNumero.startsWith("0")) {
            return "+223" + cleanNumero.substring(1);
        }
        
        // Sinon, ajouter +223 au début
        return "+223" + cleanNumero;
    }
    
    /**
     * Envoyer un SMS pour nouvelle commande
     */
    public void envoyerSMSNouvelleCommande(Commande commande) {
        Utilisateur vendeur = commande.getProduit().getVendeur();
        
        String message = String.format(
            "MussoDeme: Nouvelle commande de %s pour '%s' (Qté: %d, Total: %s FCFA). Merci !",
            commande.getAcheteur().getNom(),
            commande.getProduit().getNom(),
            commande.getQuantite(),
            commande.getMontantTotal()
        );
        
        envoyerSMS(vendeur, message);
    }
    
    /**
     * Envoyer un SMS pour paiement reçu
     */
    public void envoyerSMSPaiementRecu(Paiement paiement) {
        Utilisateur vendeur = paiement.getCommande().getProduit().getVendeur();
        
        String message = String.format(
            "MussoDeme: Paiement de %s FCFA reçu via %s pour '%s'. Votre compte a été crédité.",
            paiement.getMontant(),
            paiement.getModePaiement(),
            paiement.getCommande().getProduit().getNom()
        );
        
        envoyerSMS(vendeur, message);
    }
    
    /**
     * Envoyer un SMS pour nouveau produit
     */
    public void envoyerSMSNouveauProduit(Produit produit, Utilisateur destinataire) {
        String message = String.format(
            "MussoDeme: Nouveau produit '%s' publié par %s (Prix: %s FCFA). Découvrez-le !",
            produit.getNom(),
            produit.getVendeur().getNom(),
            produit.getPrix()
        );
        
        envoyerSMS(destinataire, message);
    }
    
    /**
     * Envoyer un SMS pour nouveau membre coopératif
     */
    public void envoyerSMSNouveauMembre(Coperative cooperative, FemmeRurale nouveauMembre, Utilisateur destinataire) {
        String message = String.format(
            "MussoDeme: %s a rejoint votre coopérative '%s'. Bienvenue !",
            nouveauMembre.getNom(),
            cooperative.getNom()
        );
        
        envoyerSMS(destinataire, message);
    }
    
    /**
     * Envoyer un SMS pour contenu partagé
     */
    public void envoyerSMSContenuPartage(Contenu contenu, Coperative cooperative, Utilisateur destinataire) {
        String message = String.format(
            "MussoDeme: Nouveau contenu '%s' partagé dans '%s'. Écoutez-le !",
            contenu.getTitre(),
            cooperative.getNom()
        );
        
        envoyerSMS(destinataire, message);
    }
    
    /**
     * Envoyer un SMS de bienvenue à l'inscription
     */
    public void envoyerSMSBienvenue(FemmeRurale femme) {
        String message = String.format(
            "Bienvenue dans MussoDeme %s ! Votre inscription est confirmée. Commencez à vendre et acheter.",
            femme.getNom()
        );
        
        envoyerSMS(femme, message);
    }
    
    /**
     * Envoyer un SMS lors de l'activation/désactivation d'un utilisateur
     */
    public void envoyerSMSActivationUtilisateur(Utilisateur utilisateur, boolean active, String adminNom) {
        String status = active ? "activé" : "désactivé";
        String message = String.format(
            "MussoDeme: Votre compte a été %s par l'administrateur %s. Contactez le support si nécessaire.",
            status, adminNom
        );
        
        envoyerSMS(utilisateur, message);
    }
    
    /**
     * Envoyer un SMS lors de l'ajout de contenu par l'admin
     */
    public void envoyerSMSNouveauContenu(Contenu contenu, Utilisateur destinataire, String adminNom) {
        String typeContenu = contenu.getTypeContenu().toString().toLowerCase();
        String message = String.format(
            "MussoDeme: Nouveau contenu %s '%s' ajouté par %s. Écoutez/regardez-le !",
            typeContenu, contenu.getTitre(), adminNom
        );
        
        envoyerSMS(destinataire, message);
    }
    
    /**
     * Envoyer un SMS lors de l'ajout d'une institution par l'admin
     */
    public void envoyerSMSNouvelleInstitution(InstitutionFinanciere institution, Utilisateur destinataire, String adminNom) {
        String message = String.format(
            "MussoDeme: Nouvelle institution '%s' ajoutée par %s. Découvrez ses services !",
            institution.getNom(), adminNom
        );
        
        envoyerSMS(destinataire, message);
    }
}