package com.mussodeme.MussoDeme.dto;

import com.mussodeme.MussoDeme.enums.TypeInfo;

import java.time.LocalDateTime;

public class PartageCooperativeDTO {
    
    private Long id;
    
    // Content information
    private Long contenuId;
    private String contenuTitre;
    private String contenuDescription;
    private String contenuMediaUrl;
    private TypeInfo contenuTypeInfo;
    
    // Cooperative information
    private Long cooperativeId;
    private String cooperativeNom;
    
    // User who shared
    private Long partageParId;
    private String partageParNom;
    private String partageParPrenom;
    
    private LocalDateTime datePartage;
    private String messageAudioUrl;  

    // Default constructor
    public PartageCooperativeDTO() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getContenuId() {
        return contenuId;
    }

    public void setContenuId(Long contenuId) {
        this.contenuId = contenuId;
    }

    public String getContenuTitre() {
        return contenuTitre;
    }

    public void setContenuTitre(String contenuTitre) {
        this.contenuTitre = contenuTitre;
    }

    public String getContenuDescription() {
        return contenuDescription;
    }

    public void setContenuDescription(String contenuDescription) {
        this.contenuDescription = contenuDescription;
    }

    public String getContenuMediaUrl() {
        return contenuMediaUrl;
    }

    public void setContenuMediaUrl(String contenuMediaUrl) {
        this.contenuMediaUrl = contenuMediaUrl;
    }

    public TypeInfo getContenuTypeInfo() {
        return contenuTypeInfo;
    }

    public void setContenuTypeInfo(TypeInfo contenuTypeInfo) {
        this.contenuTypeInfo = contenuTypeInfo;
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

    public Long getPartageParId() {
        return partageParId;
    }

    public void setPartageParId(Long partageParId) {
        this.partageParId = partageParId;
    }

    public String getPartageParNom() {
        return partageParNom;
    }

    public void setPartageParNom(String partageParNom) {
        this.partageParNom = partageParNom;
    }

    public String getPartageParPrenom() {
        return partageParPrenom;
    }

    public void setPartageParPrenom(String partageParPrenom) {
        this.partageParPrenom = partageParPrenom;
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
        if (!(o instanceof PartageCooperativeDTO)) return false;
        PartageCooperativeDTO that = (PartageCooperativeDTO) o;
        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "PartageCooperativeDTO{" +
                "id=" + id +
                ", contenuId=" + contenuId +
                ", contenuTitre='" + contenuTitre + '\'' +
                ", contenuDescription='" + contenuDescription + '\'' +
                ", contenuMediaUrl='" + contenuMediaUrl + '\'' +
                ", contenuTypeInfo=" + contenuTypeInfo +
                ", cooperativeId=" + cooperativeId +
                ", cooperativeNom='" + cooperativeNom + '\'' +
                ", partageParId=" + partageParId +
                ", partageParNom='" + partageParNom + '\'' +
                ", partageParPrenom='" + partageParPrenom + '\'' +
                ", datePartage=" + datePartage +
                ", messageAudioUrl='" + messageAudioUrl + '\'' +
                '}';
    }
}