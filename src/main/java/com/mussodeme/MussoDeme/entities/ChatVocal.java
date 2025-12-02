package com.mussodeme.MussoDeme.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Entité représentant un message vocal entre femmes rurales
 * Système asynchrone : upload audio → stockage → notification
 * Adapté pour zones rurales avec connexion faible
 */
@Entity
@Table(name = "chat_vocal")
public class ChatVocal {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * URL du fichier audio uploadé via FileController
     */
    @Column(name = "audio_url", nullable = true)
    private String audioUrl;
    
    /**
     * Durée du message vocal (ex: "00:45", "02:30")
     */
    @Column(name = "duree")
    private String duree;

    @Column(length = 1000)
    private String texte;

    @Column(name = "fichier_url")
    private String fichierUrl;
    
    /**
     * Femme qui envoie le message vocal
     */
    @ManyToOne
    @JoinColumn(name = "expediteur_id", nullable = false)
    private FemmeRurale expediteur;
    
    /**
     * Femme qui reçoit le message (null si chat de coopérative)
     */
    @ManyToOne
    @JoinColumn(name = "destinataire_id")
    private FemmeRurale destinataire;
    
    /**
     * Coopérative destinataire (null si chat privé)
     * Si renseigné, tous les membres de la coopérative peuvent écouter
     */
    @ManyToOne
    @JoinColumn(name = "cooperative_id")
    private Coperative coperative;
    
    /**
     * Date d'envoi du message
     */
    @Column(name = "date_envoi", nullable = false)
    private LocalDateTime dateEnvoi = LocalDateTime.now();
    
    /**
     * Le message a-t-il été écouté ? (pour chat privé uniquement)
     */
    @Column(name = "lu")
    private boolean lu = false;
    
    /**
     * Date de lecture du message (null si non lu)
     */
    @Column(name = "date_lecture")
    private LocalDateTime dateLecture;

    // Default constructor
    public ChatVocal() {
    }

    // Constructor with all fields
    public ChatVocal(Long id, String audioUrl, String duree, FemmeRurale expediteur, FemmeRurale destinataire, 
                    Coperative coperative, LocalDateTime dateEnvoi, boolean lu, LocalDateTime dateLecture, String texte, String fichierUrl) {
        this.id = id;
        this.audioUrl = audioUrl;
        this.duree = duree;
        this.expediteur = expediteur;
        this.destinataire = destinataire;
        this.coperative = coperative;
        this.dateEnvoi = dateEnvoi != null ? dateEnvoi : LocalDateTime.now();
        this.lu = lu;
        this.dateLecture = dateLecture;
        this.texte = texte;
        this.fichierUrl = fichierUrl;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public FemmeRurale getExpediteur() {
        return expediteur;
    }

    public void setExpediteur(FemmeRurale expediteur) {
        this.expediteur = expediteur;
    }

    public FemmeRurale getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(FemmeRurale destinataire) {
        this.destinataire = destinataire;
    }

    public Coperative getCoperative() {
        return coperative;
    }

    public void setCoperative(Coperative coperative) {
        this.coperative = coperative;
    }

    public LocalDateTime getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(LocalDateTime dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public boolean isLu() {
        return lu;
    }

    public void setLu(boolean lu) {
        this.lu = lu;
    }

    public LocalDateTime getDateLecture() {
        return dateLecture;
    }

    public void setDateLecture(LocalDateTime dateLecture) {
        this.dateLecture = dateLecture;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public String getFichierUrl() {
        return fichierUrl;
    }

    public void setFichierUrl(String fichierUrl) {
        this.fichierUrl = fichierUrl;
    }

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatVocal)) return false;
        ChatVocal chatVocal = (ChatVocal) o;
        return id != null && id.equals(chatVocal.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "ChatVocal{" +
                "id=" + id +
                ", audioUrl='" + audioUrl + '\'' +
                ", duree='" + duree + '\'' +
                ", expediteur=" + expediteur +
                ", destinataire=" + destinataire +
                ", coperative=" + coperative +
                ", dateEnvoi=" + dateEnvoi +
                ", lu=" + lu +
                ", dateLecture=" + dateLecture +
                ", texte=" + texte +
                ", fichierUrl=" + fichierUrl +
                '}';
    }
}