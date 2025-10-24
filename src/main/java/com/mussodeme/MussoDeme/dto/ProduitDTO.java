package com.mussodeme.MussoDeme.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mussodeme.MussoDeme.enums.TypeProduit;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProduitDTO {
    private Long id;
    private String nom;
    private String description;
    private String image;
    private Integer quantite;
    private Double prix;
    private TypeProduit typeProduit;       // Type de produit
    private String audioGuideUrl;           // Voice guide description product
    private Long femmeRuraleId;

    // Default constructor
    public ProduitDTO() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public TypeProduit getTypeProduit() {
        return typeProduit;
    }

    public void setTypeProduit(TypeProduit typeProduit) {
        this.typeProduit = typeProduit;
    }

    public String getAudioGuideUrl() {
        return audioGuideUrl;
    }

    public void setAudioGuideUrl(String audioGuideUrl) {
        this.audioGuideUrl = audioGuideUrl;
    }

    public Long getFemmeRuraleId() {
        return femmeRuraleId;
    }

    public void setFemmeRuraleId(Long femmeRuraleId) {
        this.femmeRuraleId = femmeRuraleId;
    }

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProduitDTO)) return false;
        ProduitDTO that = (ProduitDTO) o;
        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ProduitDTO{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", quantite=" + quantite +
                ", prix=" + prix +
                ", typeProduit=" + typeProduit +
                ", audioGuideUrl='" + audioGuideUrl + '\'' +
                ", femmeRuraleId=" + femmeRuraleId +
                '}';
    }
}