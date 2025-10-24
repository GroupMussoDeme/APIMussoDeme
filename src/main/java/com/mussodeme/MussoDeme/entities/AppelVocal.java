package com.mussodeme.MussoDeme.entities;

import com.mussodeme.MussoDeme.enums.StatutAppel;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "appel_vocal")
public class AppelVocal {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "appelant_id", nullable = false)
    private FemmeRurale appelant;
    
    @ManyToOne
    @JoinColumn(name = "appele_id")
    private FemmeRurale appele;
    
    @ManyToOne
    @JoinColumn(name = "cooperative_id")
    private Coperative cooperative;
    
    @Column(name = "audio_url")
    private String audioUrl;
    
    @Column(name = "date_appel", nullable = false)
    private LocalDateTime dateAppel;
    
    @Column(name = "duree_secondes")
    private Long dureeSecondes;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutAppel statut;
    
    @Column(name = "date_reponse")
    private LocalDateTime dateReponse;
    
    @Column(name = "message_vocal_url")
    private String messageVocalUrl; // Pour messagerie vocale
    
    @Column(name = "est_appel_groupe")
    private boolean estAppelGroupe;

    // Default constructor
    public AppelVocal() {
    }

    // Constructor with all fields
    public AppelVocal(Long id, FemmeRurale appelant, FemmeRurale appele, Coperative cooperative, String audioUrl, 
                     LocalDateTime dateAppel, Long dureeSecondes, StatutAppel statut, LocalDateTime dateReponse, 
                     String messageVocalUrl, boolean estAppelGroupe) {
        this.id = id;
        this.appelant = appelant;
        this.appele = appele;
        this.cooperative = cooperative;
        this.audioUrl = audioUrl;
        this.dateAppel = dateAppel;
        this.dureeSecondes = dureeSecondes;
        this.statut = statut;
        this.dateReponse = dateReponse;
        this.messageVocalUrl = messageVocalUrl;
        this.estAppelGroupe = estAppelGroupe;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FemmeRurale getAppelant() {
        return appelant;
    }

    public void setAppelant(FemmeRurale appelant) {
        this.appelant = appelant;
    }

    public FemmeRurale getAppele() {
        return appele;
    }

    public void setAppele(FemmeRurale appele) {
        this.appele = appele;
    }

    public Coperative getCooperative() {
        return cooperative;
    }

    public void setCooperative(Coperative cooperative) {
        this.cooperative = cooperative;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public LocalDateTime getDateAppel() {
        return dateAppel;
    }

    public void setDateAppel(LocalDateTime dateAppel) {
        this.dateAppel = dateAppel;
    }

    public Long getDureeSecondes() {
        return dureeSecondes;
    }

    public void setDureeSecondes(Long dureeSecondes) {
        this.dureeSecondes = dureeSecondes;
    }

    public StatutAppel getStatut() {
        return statut;
    }

    public void setStatut(StatutAppel statut) {
        this.statut = statut;
    }

    public LocalDateTime getDateReponse() {
        return dateReponse;
    }

    public void setDateReponse(LocalDateTime dateReponse) {
        this.dateReponse = dateReponse;
    }

    public String getMessageVocalUrl() {
        return messageVocalUrl;
    }

    public void setMessageVocalUrl(String messageVocalUrl) {
        this.messageVocalUrl = messageVocalUrl;
    }

    public boolean isEstAppelGroupe() {
        return estAppelGroupe;
    }

    public void setEstAppelGroupe(boolean estAppelGroupe) {
        this.estAppelGroupe = estAppelGroupe;
    }

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppelVocal)) return false;
        AppelVocal that = (AppelVocal) o;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "AppelVocal{" +
                "id=" + id +
                ", appelant=" + appelant +
                ", appele=" + appele +
                ", cooperative=" + cooperative +
                ", audioUrl='" + audioUrl + '\'' +
                ", dateAppel=" + dateAppel +
                ", dureeSecondes=" + dureeSecondes +
                ", statut=" + statut +
                ", dateReponse=" + dateReponse +
                ", messageVocalUrl='" + messageVocalUrl + '\'' +
                ", estAppelGroupe=" + estAppelGroupe +
                '}';
    }
}