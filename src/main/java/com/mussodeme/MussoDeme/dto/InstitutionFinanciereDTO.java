package com.mussodeme.MussoDeme.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InstitutionFinanciereDTO {
    private Long id;
    private String nom;
    private String numeroTel;
    private String description;
    private String logoUrl;

    // Default constructor
    public InstitutionFinanciereDTO() {
    }

    // Constructor with all fields
    public InstitutionFinanciereDTO(Long id, String nom, String numeroTel, String description, String logoUrl) {
        this.id = id;
        this.nom = nom;
        this.numeroTel = numeroTel;
        this.description = description;
        this.logoUrl = logoUrl;
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

    public String getNumeroTel() {
        return numeroTel;
    }

    public void setNumeroTel(String numeroTel) {
        this.numeroTel = numeroTel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InstitutionFinanciereDTO)) return false;
        InstitutionFinanciereDTO that = (InstitutionFinanciereDTO) o;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "InstitutionFinanciereDTO{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", numeroTel='" + numeroTel + '\'' +
                ", description='" + description + '\'' +
                ", logoUrl='" + logoUrl + '\'' +
                '}';
    }
}