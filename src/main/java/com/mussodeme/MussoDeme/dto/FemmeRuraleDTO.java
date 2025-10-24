package com.mussodeme.MussoDeme.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mussodeme.MussoDeme.enums.Role;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FemmeRuraleDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String localite;
    private String numeroTel;
    @JsonIgnore
    private String motCle;
    private Role role;
    private boolean active;

    // Default constructor
    public FemmeRuraleDTO() {
    }

    // Constructor with all fields
    public FemmeRuraleDTO(Long id, String nom, String prenom, String localite, String numeroTel, String motCle, Role role, boolean active) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.localite = localite;
        this.numeroTel = numeroTel;
        this.motCle = motCle;
        this.role = role;
        this.active = active;
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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getLocalite() {
        return localite;
    }

    public void setLocalite(String localite) {
        this.localite = localite;
    }

    public String getNumeroTel() {
        return numeroTel;
    }

    public void setNumeroTel(String numeroTel) {
        this.numeroTel = numeroTel;
    }

    public String getMotCle() {
        return motCle;
    }

    public void setMotCle(String motCle) {
        this.motCle = motCle;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FemmeRuraleDTO)) return false;
        FemmeRuraleDTO that = (FemmeRuraleDTO) o;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "FemmeRuraleDTO{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", localite='" + localite + '\'' +
                ", numeroTel='" + numeroTel + '\'' +
                ", motCle='" + motCle + '\'' +
                ", role=" + role +
                ", active=" + active +
                '}';
    }
}