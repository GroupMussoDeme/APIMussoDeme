package com.mussodeme.MussoDeme.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "institution_financiere")
public class InstitutionFinanciere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String numeroTel;
    private String description;
    private String logoUrl;

    @Column(precision = 15, scale = 2)
    private BigDecimal montantMin;

    @Column(precision = 15, scale = 2)
    private BigDecimal montantMax;

    private String secteurActivite;
    private String tauxInteret;

    public InstitutionFinanciere() {}

    public InstitutionFinanciere(Long id,
                                 String nom,
                                 String numeroTel,
                                 String description,
                                 String logoUrl,
                                 BigDecimal montantMin,
                                 BigDecimal montantMax,
                                 String secteurActivite,
                                 String tauxInteret) {
        this.id = id;
        this.nom = nom;
        this.numeroTel = numeroTel;
        this.description = description;
        this.logoUrl = logoUrl;
        this.montantMin = montantMin;
        this.montantMax = montantMax;
        this.secteurActivite = secteurActivite;
        this.tauxInteret = tauxInteret;
    }

    // Getters / Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getNumeroTel() { return numeroTel; }
    public void setNumeroTel(String numeroTel) { this.numeroTel = numeroTel; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }

    public BigDecimal getMontantMin() { return montantMin; }
    public void setMontantMin(BigDecimal montantMin) { this.montantMin = montantMin; }

    public BigDecimal getMontantMax() { return montantMax; }
    public void setMontantMax(BigDecimal montantMax) { this.montantMax = montantMax; }

    public String getSecteurActivite() { return secteurActivite; }
    public void setSecteurActivite(String secteurActivite) { this.secteurActivite = secteurActivite; }

    public String getTauxInteret() { return tauxInteret; }
    public void setTauxInteret(String tauxInteret) { this.tauxInteret = tauxInteret; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InstitutionFinanciere)) return false;
        InstitutionFinanciere that = (InstitutionFinanciere) o;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() { return getClass().hashCode(); }

    @Override
    public String toString() {
        return "InstitutionFinanciere{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", numeroTel='" + numeroTel + '\'' +
                ", description='" + description + '\'' +
                ", logoUrl='" + logoUrl + '\'' +
                ", montantMin=" + montantMin +
                ", montantMax=" + montantMax +
                ", secteurActivite='" + secteurActivite + '\'' +
                ", tauxInteret='" + tauxInteret + '\'' +
                '}';
    }
}
