package com.mussodeme.MussoDeme.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    // Generic
    private int status;
    private String message;

    // For login/auth (si tu gères l’authentification)
    private String token;
    private String role;
    private String expirationTime;

    // For pagination
    private Integer totalPages;
    private Long totalElements;

    // Data output optional
    private UtilisateurDTO utilisateur;
    private List<UtilisateurDTO> utilisateurs;

    private FemmeRuraleDTO femmeRurale;
    private List<FemmeRuraleDTO> femmesRurales;

    private CoperativeDTO coperative;
    private List<CoperativeDTO> coperatives;

    private AudioConseilDTO audioConseil;
    private List<AudioConseilDTO> audioConseils;

    private ProduitDTO produit;
    private List<ProduitDTO> produits;

    private CommandeDTO commande;
    private List<CommandeDTO> commandes;

    private PaiementDTO paiement;
    private List<PaiementDTO> paiements;

    private NotificationDTO notification;
    private List<NotificationDTO> notifications;

    private HistoriqueDTO historique;
    private List<HistoriqueDTO> historiques;

    private TutoDTO tuto;
    private List<TutoDTO> tutos;

    private UtilisateurAudioDTO utilisateurAudio;
    private List<UtilisateurAudioDTO> utilisateurAudios;

    private RechercheParLocalisationDTO rechercheParLocalisation;
    private List<RechercheParLocalisationDTO> recherchesParLocalisation;

    private final LocalDateTime timestamp = LocalDateTime.now();
}
