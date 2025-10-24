package com.mussodeme.MussoDeme.dto;

import com.mussodeme.MussoDeme.enums.StatutAppel;

import java.time.LocalDateTime;

public class AppelVocalDTO {
    private Long id;
    private Long appelantId;
    private String appelantNom;
    private Long appeleId;
    private String appeleNom;
    private Long cooperativeId;
    private String cooperativeNom;
    private String audioUrl;
    private LocalDateTime dateAppel;
    private Long dureeSecondes;
    private StatutAppel statut;
    private LocalDateTime dateReponse;
    private String messageVocalUrl;
    private boolean estAppelGroupe;

    // Default constructor
    public AppelVocalDTO() {
    }

    // Constructor with all fields
    public AppelVocalDTO(Long id, Long appelantId, String appelantNom, Long appeleId, String appeleNom, 
                        Long cooperativeId, String cooperativeNom, String audioUrl, LocalDateTime dateAppel, 
                        Long dureeSecondes, StatutAppel statut, LocalDateTime dateReponse, String messageVocalUrl, 
                        boolean estAppelGroupe) {
        this.id = id;
        this.appelantId = appelantId;
        this.appelantNom = appelantNom;
        this.appeleId = appeleId;
        this.appeleNom = appeleNom;
        this.cooperativeId = cooperativeId;
        this.cooperativeNom = cooperativeNom;
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

    public Long getAppelantId() {
        return appelantId;
    }

    public void setAppelantId(Long appelantId) {
        this.appelantId = appelantId;
    }

    public String getAppelantNom() {
        return appelantNom;
    }

    public void setAppelantNom(String appelantNom) {
        this.appelantNom = appelantNom;
    }

    public Long getAppeleId() {
        return appeleId;
    }

    public void setAppeleId(Long appeleId) {
        this.appeleId = appeleId;
    }

    public String getAppeleNom() {
        return appeleNom;
    }

    public void setAppeleNom(String appeleNom) {
        this.appeleNom = appeleNom;
    }

    public Long getCooperativeId() {
        return cooperativeId;
    }

    public void setCooperativeId(Long cooperativeId) {
        this.cooperativeId = cooperativeId;
    }

    public String getCooperativeNom() {
        return cooperativeNom;
    }

    public void setCooperativeNom(String cooperativeNom) {
        this.cooperativeNom = cooperativeNom;
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
        if (!(o instanceof AppelVocalDTO)) return false;
        AppelVocalDTO that = (AppelVocalDTO) o;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "AppelVocalDTO{" +
                "id=" + id +
                ", appelantId=" + appelantId +
                ", appelantNom='" + appelantNom + '\'' +
                ", appeleId=" + appeleId +
                ", appeleNom='" + appeleNom + '\'' +
                ", cooperativeId=" + cooperativeId +
                ", cooperativeNom='" + cooperativeNom + '\'' +
                ", audioUrl='" + audioUrl + '\'' +
                ", dateAppel=" + dateAppel +
                ", dureeSecondes=" + dureeSecondes +
                ", statut=" + statut +
                ", dateReponse=" + dateReponse +
                ", messageVocalUrl='" + messageVocalUrl + '\'' +
                ", estAppelGroupe=" + estAppelGroupe +
                '}';
    }

    // Builder pattern
    public static class Builder {
        private Long id;
        private Long appelantId;
        private String appelantNom;
        private Long appeleId;
        private String appeleNom;
        private Long cooperativeId;
        private String cooperativeNom;
        private String audioUrl;
        private LocalDateTime dateAppel;
        private Long dureeSecondes;
        private StatutAppel statut;
        private LocalDateTime dateReponse;
        private String messageVocalUrl;
        private boolean estAppelGroupe;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder appelantId(Long appelantId) {
            this.appelantId = appelantId;
            return this;
        }

        public Builder appelantNom(String appelantNom) {
            this.appelantNom = appelantNom;
            return this;
        }

        public Builder appeleId(Long appeleId) {
            this.appeleId = appeleId;
            return this;
        }

        public Builder appeleNom(String appeleNom) {
            this.appeleNom = appeleNom;
            return this;
        }

        public Builder cooperativeId(Long cooperativeId) {
            this.cooperativeId = cooperativeId;
            return this;
        }

        public Builder cooperativeNom(String cooperativeNom) {
            this.cooperativeNom = cooperativeNom;
            return this;
        }

        public Builder audioUrl(String audioUrl) {
            this.audioUrl = audioUrl;
            return this;
        }

        public Builder dateAppel(LocalDateTime dateAppel) {
            this.dateAppel = dateAppel;
            return this;
        }

        public Builder dureeSecondes(Long dureeSecondes) {
            this.dureeSecondes = dureeSecondes;
            return this;
        }

        public Builder statut(StatutAppel statut) {
            this.statut = statut;
            return this;
        }

        public Builder dateReponse(LocalDateTime dateReponse) {
            this.dateReponse = dateReponse;
            return this;
        }

        public Builder messageVocalUrl(String messageVocalUrl) {
            this.messageVocalUrl = messageVocalUrl;
            return this;
        }

        public Builder estAppelGroupe(boolean estAppelGroupe) {
            this.estAppelGroupe = estAppelGroupe;
            return this;
        }

        public AppelVocalDTO build() {
            return new AppelVocalDTO(id, appelantId, appelantNom, appeleId, appeleNom, 
                                   cooperativeId, cooperativeNom, audioUrl, dateAppel, 
                                   dureeSecondes, statut, dateReponse, messageVocalUrl, 
                                   estAppelGroupe);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}