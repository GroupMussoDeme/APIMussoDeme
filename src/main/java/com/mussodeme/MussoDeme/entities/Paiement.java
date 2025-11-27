package com.mussodeme.MussoDeme.entities;

import com.mussodeme.MussoDeme.enums.ModePaiement;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "paiement")
public class Paiement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime datePaiement;

    @Enumerated(EnumType.STRING)
    @Column(name = "mode_paiement", length = 20, nullable = false)
    private ModePaiement modePaiement;

    private Double montant;

    @ManyToOne
    @JoinColumn(name = "id_commande")
    private Commande commande;

    @ManyToOne
    @JoinColumn(name = "id_acheteur")
    private Utilisateur acheteur;

    // Default constructor
    public Paiement() {
    }

    // Constructor with all fields
    public Paiement(Long id, LocalDateTime datePaiement, ModePaiement modePaiement, Double montant, 
                   Commande commande, Utilisateur acheteur) {
        this.id = id;
        this.datePaiement = datePaiement;
        this.modePaiement = modePaiement;
        this.montant = montant;
        this.commande = commande;
        this.acheteur = acheteur;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(LocalDateTime datePaiement) {
        this.datePaiement = datePaiement;
    }

    public ModePaiement getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(ModePaiement modePaiement) {
        this.modePaiement = modePaiement;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public Utilisateur getAcheteur() {
        return acheteur;
    }

    public void setAcheteur(Utilisateur acheteur) {
        this.acheteur = acheteur;
    }

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Paiement)) return false;
        Paiement paiement = (Paiement) o;
        return id != null && id.equals(paiement.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Paiement{" +
                "id=" + id +
                ", datePaiement=" + datePaiement +
                ", modePaiement=" + modePaiement +
                ", montant=" + montant +
                ", commande=" + commande +
                ", acheteur=" + acheteur +
                '}';
    }
}