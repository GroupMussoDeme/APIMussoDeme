package com.mussodeme.MussoDeme.services;

import com.mussodeme.MussoDeme.entities.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.logging.Logger;

@Service
public class EmailService {

    private static final Logger logger = Logger.getLogger(EmailService.class.getName());

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.name:MussoDeme}")
    private String appName;

    // Constructor for dependency injection
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Envoi d'email asynchrone pour ne pas bloquer le thread principal
     */
    @Async
    public void envoyerEmail(String destinataire, String sujet, String contenu) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(destinataire);
            message.setSubject(sujet);
            message.setText(contenu);
            
            mailSender.send(message);
            logger.info("Email envoyé avec succès à : " + destinataire);
        } catch (Exception e) {
            logger.severe("Erreur lors de l'envoi de l'email à " + destinataire + " : " + e.getMessage());
        }
    }

    /**
     * Envoi d'email HTML asynchrone
     */
    @Async
    public void envoyerEmailHtml(String destinataire, String sujet, String contenuHtml) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(destinataire);
            helper.setSubject(sujet);
            helper.setText(contenuHtml, true);
            
            mailSender.send(message);
            logger.info("Email HTML envoyé avec succès à : " + destinataire);
        } catch (MessagingException e) {
            logger.severe("Erreur lors de l'envoi de l'email HTML à " + destinataire + " : " + e.getMessage());
        }
    }

    // ==================== TEMPLATES D'EMAILS ====================

    /**
     * Email pour nouvelle commande (au vendeur)
     */
    @Async
    public void envoyerEmailNouvelleCommande(Commande commande, Utilisateur vendeur) {
        if (vendeur.getEmail() == null || vendeur.getEmail().isEmpty()) {
            logger.warning("Le vendeur " + vendeur.getNom() + " n'a pas d'email configuré");
            return;
        }

        String sujet = "🛒 Nouvelle commande reçue - " + appName;
        String contenu = creerTemplateNouvelleCommande(commande, vendeur);
        
        envoyerEmailHtml(vendeur.getEmail(), sujet, contenu);
    }

    /**
     * Email pour paiement reçu (au vendeur)
     */
    @Async
    public void envoyerEmailPaiementRecu(Paiement paiement, Utilisateur vendeur) {
        if (vendeur.getEmail() == null || vendeur.getEmail().isEmpty()) {
            logger.warning("Le vendeur " + vendeur.getNom() + " n'a pas d'email configuré");
            return;
        }

        String sujet = " Paiement reçu - " + appName;
        String contenu = creerTemplatePaiementRecu(paiement, vendeur);
        
        envoyerEmailHtml(vendeur.getEmail(), sujet, contenu);
    }

    /**
     * Email pour nouvelle publication de produit (aux membres de la coopérative)
     */
    @Async
    public void envoyerEmailNouveauProduit(Produit produit, Utilisateur destinataire) {
        if (destinataire.getEmail() == null || destinataire.getEmail().isEmpty()) {
            return;
        }

        String sujet = " Nouveau produit publié - " + appName;
        String contenu = creerTemplateNouveauProduit(produit, destinataire);
        
        envoyerEmailHtml(destinataire.getEmail(), sujet, contenu);
    }

    /**
     * Email pour nouveau membre dans coopérative
     */
    @Async
    public void envoyerEmailNouveauMembreCooperative(Coperative cooperative, FemmeRurale nouveauMembre, Utilisateur destinataire) {
        if (destinataire.getEmail() == null || destinataire.getEmail().isEmpty()) {
            return;
        }

        String sujet = " Nouveau membre dans " + cooperative.getNom() + " - " + appName;
        String contenu = creerTemplateNouveauMembre(cooperative, nouveauMembre, destinataire);
        
        envoyerEmailHtml(destinataire.getEmail(), sujet, contenu);
    }

    /**
     * Email pour contenu partagé dans coopérative
     */
    @Async
    public void envoyerEmailContenuPartage(Contenu contenu, Coperative cooperative, Utilisateur destinataire) {
        if (destinataire.getEmail() == null || destinataire.getEmail().isEmpty()) {
            return;
        }

        String sujet = " Nouveau contenu partagé dans " + cooperative.getNom() + " - " + appName;
        String contenuHtml = creerTemplateContenuPartage(contenu, cooperative, destinataire);
        
        envoyerEmailHtml(destinataire.getEmail(), sujet, contenuHtml);
    }

    // ==================== CRÉATION DES TEMPLATES HTML ====================

    private String creerTemplateNouvelleCommande(Commande commande, Utilisateur vendeur) {
        NumberFormat formatMontant = NumberFormat.getCurrencyInstance(Locale.FRANCE);
        
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; border-radius: 5px; }
                    .content { background-color: #f9f9f9; padding: 20px; margin: 20px 0; border-radius: 5px; }
                    .info-row { margin: 10px 0; padding: 10px; background-color: white; border-left: 4px solid #4CAF50; }
                    .footer { text-align: center; color: #777; font-size: 12px; margin-top: 20px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🛒 Nouvelle Commande</h1>
                    </div>
                    <div class="content">
                        <p>Bonjour <strong>%s</strong>,</p>
                        <p>Vous avez reçu une nouvelle commande !</p>
                        
                        <div class="info-row">
                            <strong>Produit :</strong> %s
                        </div>
                        <div class="info-row">
                            <strong>Quantité :</strong> %d
                        </div>
                        <div class="info-row">
                            <strong>Prix unitaire :</strong> %s FCFA
                        </div>
                        <div class="info-row">
                            <strong>Montant total :</strong> %s FCFA
                        </div>
                        <div class="info-row">
                            <strong>Acheteur :</strong> %s
                        </div>
                        <div class="info-row">
                            <strong>Statut :</strong> %s
                        </div>
                        
                        <p style="margin-top: 20px;">Connectez-vous à votre compte pour plus de détails.</p>
                    </div>
                    <div class="footer">
                        <p>Cet email a été envoyé automatiquement par %s</p>
                        <p>Merci d'utiliser notre plateforme ! 🌾</p>
                    </div>
                </div>
            </body>
            </html>
            """,
            vendeur.getNom(),
            commande.getProduit().getNom(),
            commande.getQuantite(),
            commande.getProduit().getPrix(),
            commande.getMontantTotal(),
            commande.getAcheteur().getNom(),
            commande.getStatut(),
            appName
        );
    }

    private String creerTemplatePaiementRecu(Paiement paiement, Utilisateur vendeur) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #2196F3; color: white; padding: 20px; text-align: center; border-radius: 5px; }
                    .content { background-color: #f9f9f9; padding: 20px; margin: 20px 0; border-radius: 5px; }
                    .info-row { margin: 10px 0; padding: 10px; background-color: white; border-left: 4px solid #2196F3; }
                    .amount { font-size: 24px; color: #4CAF50; font-weight: bold; text-align: center; padding: 15px; background-color: #e8f5e9; border-radius: 5px; }
                    .footer { text-align: center; color: #777; font-size: 12px; margin-top: 20px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>💰 Paiement Reçu</h1>
                    </div>
                    <div class="content">
                        <p>Bonjour <strong>%s</strong>,</p>
                        <p>Vous avez reçu un paiement !</p>
                        
                        <div class="amount">
                            %s FCFA
                        </div>
                        
                        <div class="info-row">
                            <strong>Mode de paiement :</strong> %s
                        </div>
                        <div class="info-row">
                            <strong>Payé par :</strong> %s
                        </div>
                        <div class="info-row">
                            <strong>Date :</strong> %s
                        </div>
                        <div class="info-row">
                            <strong>Produit :</strong> %s
                        </div>
                        
                        <p style="margin-top: 20px;">Le montant sera disponible dans votre compte.</p>
                    </div>
                    <div class="footer">
                        <p>Cet email a été envoyé automatiquement par %s</p>
                        <p>Merci d'utiliser notre plateforme ! 🌾</p>
                    </div>
                </div>
            </body>
            </html>
            """,
            vendeur.getNom(),
            paiement.getMontant(),
            paiement.getModePaiement(),
            paiement.getAcheteur().getNom(),
            paiement.getDatePaiement(),
            paiement.getCommande().getProduit().getNom(),
            appName
        );
    }

    private String creerTemplateNouveauProduit(Produit produit, Utilisateur destinataire) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #FF9800; color: white; padding: 20px; text-align: center; border-radius: 5px; }
                    .content { background-color: #f9f9f9; padding: 20px; margin: 20px 0; border-radius: 5px; }
                    .info-row { margin: 10px 0; padding: 10px; background-color: white; border-left: 4px solid #FF9800; }
                    .product-name { font-size: 22px; font-weight: bold; color: #FF9800; text-align: center; margin: 15px 0; }
                    .footer { text-align: center; color: #777; font-size: 12px; margin-top: 20px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1> Nouveau Produit</h1>
                    </div>
                    <div class="content">
                        <p>Bonjour <strong>%s</strong>,</p>
                        <p>Un nouveau produit a été publié sur la plateforme !</p>
                        
                        <div class="product-name">
                            %s
                        </div>
                        
                        <div class="info-row">
                            <strong>Type :</strong> %s
                        </div>
                        <div class="info-row">
                            <strong>Prix :</strong> %s FCFA
                        </div>
                        <div class="info-row">
                            <strong>Stock disponible :</strong> %d unités
                        </div>
                        <div class="info-row">
                            <strong>Description :</strong> %s
                        </div>
                        <div class="info-row">
                            <strong>Publié par :</strong> %s
                        </div>
                        
                        <p style="margin-top: 20px;">Connectez-vous pour passer commande !</p>
                    </div>
                    <div class="footer">
                        <p>Cet email a été envoyé automatiquement par %s</p>
                        <p>Merci d'utiliser notre plateforme ! 🌾</p>
                    </div>
                </div>
            </body>
            </html>
            """,
            destinataire.getNom(),
            produit.getNom(),
            produit.getTypeProduit(),
            produit.getPrix(),
            produit.getStock(),
            produit.getDescription(),
            produit.getVendeur().getNom(),
            appName
        );
    }

    private String creerTemplateNouveauMembre(Coperative cooperative, FemmeRurale nouveauMembre, Utilisateur destinataire) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #9C27B0; color: white; padding: 20px; text-align: center; border-radius: 5px; }
                    .content { background-color: #f9f9f9; padding: 20px; margin: 20px 0; border-radius: 5px; }
                    .info-row { margin: 10px 0; padding: 10px; background-color: white; border-left: 4px solid #9C27B0; }
                    .footer { text-align: center; color: #777; font-size: 12px; margin-top: 20px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1> Nouveau Membre</h1>
                    </div>
                    <div class="content">
                        <p>Bonjour <strong>%s</strong>,</p>
                        <p>Un nouveau membre a rejoint votre coopérative <strong>%s</strong> !</p>
                        
                        <div class="info-row">
                            <strong>Nouveau membre :</strong> %s
                        </div>
                        <div class="info-row">
                            <strong>Téléphone :</strong> %s
                        </div>
                        <div class="info-row">
                            <strong>Localisation :</strong> %s
                        </div>
                        
                        <p style="margin-top: 20px;">Souhaitez la bienvenue au nouveau membre !</p>
                    </div>
                    <div class="footer">
                        <p>Cet email a été envoyé automatiquement par %s</p>
                        <p>Merci d'utiliser notre plateforme ! 🌾</p>
                    </div>
                </div>
            </body>
            </html>
            """,
            destinataire.getNom(),
            cooperative.getNom(),
            nouveauMembre.getNom(),
            nouveauMembre.getNumeroTel(),
            nouveauMembre.getLocalite(),
            appName
        );
    }

    private String creerTemplateContenuPartage(Contenu contenu, Coperative cooperative, Utilisateur destinataire) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #00BCD4; color: white; padding: 20px; text-align: center; border-radius: 5px; }
                    .content { background-color: #f9f9f9; padding: 20px; margin: 20px 0; border-radius: 5px; }
                    .info-row { margin: 10px 0; padding: 10px; background-color: white; border-left: 4px solid #00BCD4; }
                    .footer { text-align: center; color: #777; font-size: 12px; margin-top: 20px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1> Nouveau Contenu Partagé</h1>
                    </div>
                    <div class="content">
                        <p>Bonjour <strong>%s</strong>,</p>
                        <p>Un nouveau contenu a été partagé dans votre coopérative <strong>%s</strong> !</p>
                        
                        <div class="info-row">
                            <strong>Titre :</strong> %s
                        </div>
                        <div class="info-row">
                            <strong>Type :</strong> %s
                        </div>
                        <div class="info-row">
                            <strong>Catégorie :</strong> %s
                        </div>
                        <div class="info-row">
                            <strong>Description :</strong> %s
                        </div>
                        
                        <p style="margin-top: 20px;">Connectez-vous pour consulter ce contenu !</p>
                    </div>
                    <div class="footer">
                        <p>Cet email a été envoyé automatiquement par %s</p>
                        <p>Merci d'utiliser notre plateforme ! 🌾</p>
                    </div>
                </div>
            </body>
            </html>
            """,
            destinataire.getNom(),
            cooperative.getNom(),
            contenu.getTitre(),
            contenu.getTypeContenu(),
            contenu.getTypeInfo(),
            contenu.getDescription(),
            appName
        );
    }
}