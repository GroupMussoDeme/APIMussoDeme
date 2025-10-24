package com.mussodeme.MussoDeme.entities;

import com.mussodeme.MussoDeme.enums.StatutCommande;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "commande")
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantite;

    @Enumerated(EnumType.STRING)
    private StatutCommande statutCommande;

    private LocalDateTime dateAchat;

    @ManyToOne
    @JoinColumn(name = "acheteur_id", nullable = false)
    private Utilisateur acheteur;

    @ManyToOne
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    private List<Paiement> paiement;

    // Default constructor
    public Commande() {
    }

    // Constructor with all fields
    public Commande(Long id, Integer quantite, StatutCommande statutCommande, LocalDateTime dateAchat, 
                   Utilisateur acheteur, Produit produit, List<Paiement> paiement) {
        this.id = id;
        this.quantite = quantite;
        this.statutCommande = statutCommande;
        this.dateAchat = dateAchat;
        this.acheteur = acheteur;
        this.produit = produit;
        this.paiement = paiement;
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

    // Adding missing getStatut() method as an alias for getStatutCommande()
    public StatutCommande getStatut() {
        return statutCommande;
    }

    // Adding missing getMontantTotal() method
    public Double getMontantTotal() {
        if (quantite != null && produit != null && produit.getPrix() != null) {
            return quantite * produit.getPrix();
        }
        return 0.0;
    }

    public LocalDateTime getDateAchat() {
        return dateAchat;
    }

    public void setDateAchat(LocalDateTime dateAchat) {
        this.dateAchat = dateAchat;
    }

    public Utilisateur getAcheteur() {
        return acheteur;
    }

    public void setAcheteur(Utilisateur acheteur) {
        this.acheteur = acheteur;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public List<Paiement> getPaiement() {
        return paiement;
    }

    public void setPaiement(List<Paiement> paiement) {
        this.paiement = paiement;
    }

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Commande)) return false;
        Commande commande = (Commande) o;
        return id != null && id.equals(commande.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Commande{" +
                "id=" + id +
                ", quantite=" + quantite +
                ", statutCommande=" + statutCommande +
                ", dateAchat=" + dateAchat +
                ", acheteur=" + acheteur +
                ", produit=" + produit +
                '}';
    }
}