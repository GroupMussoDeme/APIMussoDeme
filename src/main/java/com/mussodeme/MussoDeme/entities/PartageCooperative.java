package com.mussodeme.MussoDeme.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entité représentant le partage d'un contenu éducatif 
 * (audio santé, vidéo formation, etc.) dans une coopérative
 * Permet aux femmes de partager des ressources utiles avec leur groupe
 */
@Entity
@Table(name = "partage_cooperative")
public class PartageCooperative {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Contenu partagé (audio ou vidéo éducatif)
     */
    @ManyToOne
    @JoinColumn(name = "contenu_id", nullable = false)
    private Contenu contenu;
    
    /**
     * Coopérative dans laquelle le contenu est partagé
     */
    @ManyToOne
    @JoinColumn(name = "cooperative_id", nullable = false)
    private Coperative coperative;
    
    /**
     * Femme qui a partagé le contenu
     */
    @ManyToOne
    @JoinColumn(name = "partage_par_id", nullable = false)
    private FemmeRurale partagePar;
    
    /**
     * Date du partage
     */
    @Column(name = "date_partage", nullable = false)
    private LocalDateTime datePartage = LocalDateTime.now();
    
    /**
     * Message vocal optionnel pour accompagner le partage
     * Ex: "Écoutez cet audio sur la nutrition, c'est très utile"
     */
    @Column(name = "message_audio_url")
    private String messageAudioUrl;

    // Default constructor
    public PartageCooperative() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Contenu getContenu() {
        return contenu;
    }

    public void setContenu(Contenu contenu) {
        this.contenu = contenu;
    }

    public Coperative getCoperative() {
        return coperative;
    }

    public void setCoperative(Coperative coperative) {
        this.coperative = coperative;
    }

    public FemmeRurale getPartagePar() {
        return partagePar;
    }

    public void setPartagePar(FemmeRurale partagePar) {
        this.partagePar = partagePar;
    }

    public LocalDateTime getDatePartage() {
        return datePartage;
    }

    public void setDatePartage(LocalDateTime datePartage) {
        this.datePartage = datePartage;
    }

    public String getMessageAudioUrl() {
        return messageAudioUrl;
    }

    public void setMessageAudioUrl(String messageAudioUrl) {
        this.messageAudioUrl = messageAudioUrl;
    }

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PartageCooperative)) return false;
        PartageCooperative that = (PartageCooperative) o;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "PartageCooperative{" +
                "id=" + id +
                ", contenu=" + contenu +
                ", coperative=" + coperative +
                ", partagePar=" + partagePar +
                ", datePartage=" + datePartage +
                ", messageAudioUrl='" + messageAudioUrl + '\'' +
                '}';
    }
}