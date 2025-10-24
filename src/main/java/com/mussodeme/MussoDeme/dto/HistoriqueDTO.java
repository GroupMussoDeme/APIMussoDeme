package com.mussodeme.MussoDeme.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mussodeme.MussoDeme.enums.TypeHistoriques;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HistoriqueDTO {
    
    private Long id;
    private TypeHistoriques typeHistoriques;
    private LocalDateTime dateAction;
    
    // User info
    private Long utilisateurId;
    private String utilisateurNom;
    private String utilisateurPrenom;
    
    // Entity info
    private Long entiteId;
    private String entiteType;
    private String description;
    private Double montant;

    // Default constructor
    public HistoriqueDTO() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeHistoriques getTypeHistoriques() {
        return typeHistoriques;
    }

    public void setTypeHistoriques(TypeHistoriques typeHistoriques) {
        this.typeHistoriques = typeHistoriques;
    }

    public LocalDateTime getDateAction() {
        return dateAction;
    }

    public void setDateAction(LocalDateTime dateAction) {
        this.dateAction = dateAction;
    }

    public Long getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public String getUtilisateurNom() {
        return utilisateurNom;
    }

    public void setUtilisateurNom(String utilisateurNom) {
        this.utilisateurNom = utilisateurNom;
    }

    public String getUtilisateurPrenom() {
        return utilisateurPrenom;
    }

    public void setUtilisateurPrenom(String utilisateurPrenom) {
        this.utilisateurPrenom = utilisateurPrenom;
    }

    public Long getEntiteId() {
        return entiteId;
    }

    public void setEntiteId(Long entiteId) {
        this.entiteId = entiteId;
    }

    public String getEntiteType() {
        return entiteType;
    }

    public void setEntiteType(String entiteType) {
        this.entiteType = entiteType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HistoriqueDTO)) return false;
        HistoriqueDTO that = (HistoriqueDTO) o;
        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "HistoriqueDTO{" +
                "id=" + id +
                ", typeHistoriques=" + typeHistoriques +
                ", dateAction=" + dateAction +
                ", utilisateurId=" + utilisateurId +
                ", utilisateurNom='" + utilisateurNom + '\'' +
                ", utilisateurPrenom='" + utilisateurPrenom + '\'' +
                ", entiteId=" + entiteId +
                ", entiteType='" + entiteType + '\'' +
                ", description='" + description + '\'' +
                ", montant=" + montant +
                '}';
    }
}