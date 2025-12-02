package com.mussodeme.MussoDeme.dto;

import java.time.LocalDateTime;

public class ChatVocalDTO {
    
    private Long id;
    private String audioUrl;
    private String duree;
    
    // Sender information
    private Long expediteurId;
    private String expediteurNom;
    private String expediteurPrenom;

    private String texte;

    private String fichierUrl;
    
    // Receiver information (null for cooperative chat)
    private Long destinataireId;
    private String destinataireNom;
    private String destinatairePrenom;
    
    // Cooperative information (null for private chat)
    private Long cooperativeId;
    private String cooperativeNom;
    
    private LocalDateTime dateEnvoi;
    private boolean lu;
    private LocalDateTime dateLecture;

    // Default constructor
    public ChatVocalDTO() {
    }

    // Constructor with all fields
    public ChatVocalDTO(Long id, String audioUrl, String duree, Long expediteurId, String expediteurNom, 
                       String expediteurPrenom, Long destinataireId, String destinataireNom, String destinatairePrenom, 
                       Long cooperativeId, String cooperativeNom, LocalDateTime dateEnvoi, boolean lu, 
                       LocalDateTime dateLecture, String texte, String fichierUrl) {
        this.id = id;
        this.audioUrl = audioUrl;
        this.duree = duree;
        this.expediteurId = expediteurId;
        this.expediteurNom = expediteurNom;
        this.expediteurPrenom = expediteurPrenom;
        this.destinataireId = destinataireId;
        this.destinataireNom = destinataireNom;
        this.destinatairePrenom = destinatairePrenom;
        this.cooperativeId = cooperativeId;
        this.cooperativeNom = cooperativeNom;
        this.dateEnvoi = dateEnvoi;
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

    public Long getExpediteurId() {
        return expediteurId;
    }

    public void setExpediteurId(Long expediteurId) {
        this.expediteurId = expediteurId;
    }

    public String getExpediteurNom() {
        return expediteurNom;
    }

    public void setExpediteurNom(String expediteurNom) {
        this.expediteurNom = expediteurNom;
    }

    public String getExpediteurPrenom() {
        return expediteurPrenom;
    }

    public void setExpediteurPrenom(String expediteurPrenom) {
        this.expediteurPrenom = expediteurPrenom;
    }

    public Long getDestinataireId() {
        return destinataireId;
    }

    public void setDestinataireId(Long destinataireId) {
        this.destinataireId = destinataireId;
    }

    public String getDestinataireNom() {
        return destinataireNom;
    }

    public void setDestinataireNom(String destinataireNom) {
        this.destinataireNom = destinataireNom;
    }

    public String getDestinatairePrenom() {
        return destinatairePrenom;
    }

    public void setDestinatairePrenom(String destinatairePrenom) {
        this.destinatairePrenom = destinatairePrenom;
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
        if (!(o instanceof ChatVocalDTO)) return false;
        ChatVocalDTO that = (ChatVocalDTO) o;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "ChatVocalDTO{" +
                "id=" + id +
                ", audioUrl='" + audioUrl + '\'' +
                ", duree='" + duree + '\'' +
                ", expediteurId=" + expediteurId +
                ", expediteurNom='" + expediteurNom + '\'' +
                ", expediteurPrenom='" + expediteurPrenom + '\'' +
                ", destinataireId=" + destinataireId +
                ", destinataireNom='" + destinataireNom + '\'' +
                ", destinatairePrenom='" + destinatairePrenom + '\'' +
                ", cooperativeId=" + cooperativeId +
                ", cooperativeNom='" + cooperativeNom + '\'' +
                ", dateEnvoi=" + dateEnvoi +
                ", lu=" + lu +
                ", dateLecture=" + dateLecture +
                ", texte=" + texte +
                ", fichierUrl=" + fichierUrl +
                '}';
    }

    // Builder pattern
    public static class Builder {
        private Long id;
        private String audioUrl;
        private String duree;
        private Long expediteurId;
        private String expediteurNom;
        private String expediteurPrenom;
        private Long destinataireId;
        private String destinataireNom;
        private String destinatairePrenom;
        private Long cooperativeId;
        private String cooperativeNom;
        private LocalDateTime dateEnvoi;
        private boolean lu;
        private LocalDateTime dateLecture;
        private String texte;
        private String fichierUrl;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder audioUrl(String audioUrl) {
            this.audioUrl = audioUrl;
            return this;
        }

        public Builder duree(String duree) {
            this.duree = duree;
            return this;
        }

        public Builder expediteurId(Long expediteurId) {
            this.expediteurId = expediteurId;
            return this;
        }

        public Builder expediteurNom(String expediteurNom) {
            this.expediteurNom = expediteurNom;
            return this;
        }

        public Builder expediteurPrenom(String expediteurPrenom) {
            this.expediteurPrenom = expediteurPrenom;
            return this;
        }

        public Builder destinataireId(Long destinataireId) {
            this.destinataireId = destinataireId;
            return this;
        }

        public Builder destinataireNom(String destinataireNom) {
            this.destinataireNom = destinataireNom;
            return this;
        }

        public Builder destinatairePrenom(String destinatairePrenom) {
            this.destinatairePrenom = destinatairePrenom;
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

        public Builder dateEnvoi(LocalDateTime dateEnvoi) {
            this.dateEnvoi = dateEnvoi;
            return this;
        }

        public Builder lu(boolean lu) {
            this.lu = lu;
            return this;
        }

        public Builder dateLecture(LocalDateTime dateLecture) {
            this.dateLecture = dateLecture;
            return this;
        }

        public Builder texte(String texte) {
            this.texte = texte;
            return this;
        }

        public Builder fichierUrl(String fichierUrl) {
            this.fichierUrl = fichierUrl;
            return this;
        }

        public ChatVocalDTO build() {
            return new ChatVocalDTO(id, audioUrl, duree, expediteurId, expediteurNom, 
                                  expediteurPrenom, destinataireId, destinataireNom, 
                                  destinatairePrenom, cooperativeId, cooperativeNom, 
                                  dateEnvoi, lu, dateLecture, texte, fichierUrl);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}