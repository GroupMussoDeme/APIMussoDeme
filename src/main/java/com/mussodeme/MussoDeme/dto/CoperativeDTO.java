package com.mussodeme.MussoDeme.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mussodeme.MussoDeme.entities.Appartenance;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoperativeDTO {
    private Long id;
    private String nom;
    private String description;
    private int nbrMembres;
    private List<Appartenance> appartenances;

    // Default constructor
    public CoperativeDTO() {
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

    public int getNbrMembres() {
        return nbrMembres;
    }

    public void setNbrMembres(int nbrMembres) {
        this.nbrMembres = nbrMembres;
    }

    public List<Appartenance> getAppartenances() {
        return appartenances;
    }

    public void setAppartenances(List<Appartenance> appartenances) {
        this.appartenances = appartenances;
    }

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CoperativeDTO)) return false;
        CoperativeDTO that = (CoperativeDTO) o;
        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "CoperativeDTO{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", nbrMembres=" + nbrMembres +
                ", appartenances=" + appartenances +
                '}';
    }
}