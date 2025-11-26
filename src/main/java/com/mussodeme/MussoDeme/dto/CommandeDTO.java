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

    private Double montantTotal;      // total = prix * quantite

    // ================== CHAMPS AJOUTÉS ==================

    // Produit complet pour le front (Flutter lit json['produit'])
    private ProduitDTO produit;       // NEW

    // Infos vendeuse = femmeRurale du produit
    private Long vendeuseId;          // NEW
    private String vendeuseNom;       // NEW

    // ====================================================

    // Default constructor
    public CommandeDTO() {
    }

    // Constructor avec tous les champs (tu peux le garder ou l’adapter)
    public CommandeDTO(Long id,
                       Integer quantite,
                       StatutCommande statutCommande,
                       LocalDateTime dateAchat,
                       Long acheteurId,
                       Long produitId,
                       Long paiementId,
                       Double montantTotal,
                       ProduitDTO produit,
                       Long vendeuseId,
                       String vendeuseNom) {
        this.id = id;
        this.quantite = quantite;
        this.statutCommande = statutCommande;
        this.dateAchat = dateAchat;
        this.acheteurId = acheteurId;
        this.produitId = produitId;
        this.paiementId = paiementId;
        this.montantTotal = montantTotal;
        this.produit = produit;
        this.vendeuseId = vendeuseId;
        this.vendeuseNom = vendeuseNom;
    }

    // ================== GETTERS / SETTERS ==================

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

    public Double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(Double montantTotal) {
        this.montantTotal = montantTotal;
    }

    // ---- NEW : produit, vendeuseId, vendeuseNom ----

    public ProduitDTO getProduit() {
        return produit;
    }

    public void setProduit(ProduitDTO produit) {
        this.produit = produit;
    }

    public Long getVendeuseId() {
        return vendeuseId;
    }

    public void setVendeuseId(Long vendeuseId) {
        this.vendeuseId = vendeuseId;
    }

    public String getVendeuseNom() {
        return vendeuseNom;
    }

    public void setVendeuseNom(String vendeuseNom) {
        this.vendeuseNom = vendeuseNom;
    }

    // ================== equals / hashCode / toString ==================

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
                ", produit=" + produit +
                ", vendeuseId=" + vendeuseId +
                ", vendeuseNom='" + vendeuseNom + '\'' +
                '}';
    }
}
