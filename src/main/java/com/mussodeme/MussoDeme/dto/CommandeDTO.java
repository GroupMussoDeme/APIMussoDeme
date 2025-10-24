package com.mussodeme.MussoDeme.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mussodeme.MussoDeme.enums.StatutCommande;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommandeDTO {
    private Long id;
    private Integer quantite;
    private StatutCommande statutCommande;
    private LocalDateTime dateAchat;
    private Long acheteurId;
    private Long produitId;
    private Long paiementId;
    private Double montantTotal; // Adding montantTotal field

    // Default constructor
    public CommandeDTO() {
    }

    // Constructor with all fields
    public CommandeDTO(Long id, Integer quantite, StatutCommande statutCommande, LocalDateTime dateAchat, 
                      Long acheteurId, Long produitId, Long paiementId, Double montantTotal) {
        this.id = id;
        this.quantite = quantite;
        this.statutCommande = statutCommande;
        this.dateAchat = dateAchat;
        this.acheteurId = acheteurId;
        this.produitId = produitId;
        this.paiementId = paiementId;
        this.montantTotal = montantTotal;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public StatutCommande getStatutCommande() {
        return statutCommande;
    }

    public void setStatutCommande(StatutCommande statutCommande) {
        this.statutCommande = statutCommande;
    }

    public LocalDateTime getDateAchat() {
        return dateAchat;
    }

    public void setDateAchat(LocalDateTime dateAchat) {
        this.dateAchat = dateAchat;
    }

    public Long getAcheteurId() {
        return acheteurId;
    }

    public void setAcheteurId(Long acheteurId) {
        this.acheteurId = acheteurId;
    }

    public Long getProduitId() {
        return produitId;
    }

    public void setProduitId(Long produitId) {
        this.produitId = produitId;
    }

    public Long getPaiementId() {
        return paiementId;
    }

    public void setPaiementId(Long paiementId) {
        this.paiementId = paiementId;
    }
    
    // Adding getMontantTotal() method
    public Double getMontantTotal() {
        return montantTotal;
    }
    
    public void setMontantTotal(Double montantTotal) {
        this.montantTotal = montantTotal;
    }

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommandeDTO)) return false;
        CommandeDTO that = (CommandeDTO) o;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "CommandeDTO{" +
                "id=" + id +
                ", quantite=" + quantite +
                ", statutCommande=" + statutCommande +
                ", dateAchat=" + dateAchat +
                ", acheteurId=" + acheteurId +
                ", produitId=" + produitId +
                ", paiementId=" + paiementId +
                ", montantTotal=" + montantTotal +
                '}';
    }
}