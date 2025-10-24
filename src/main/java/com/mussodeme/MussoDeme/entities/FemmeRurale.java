package com.mussodeme.MussoDeme.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class FemmeRurale extends Utilisateur {

    @OneToMany(mappedBy = "femmeRurale")
    private List<Produit> produits;

    @OneToMany(mappedBy = "femmeRurale")
    private List<Appartenance> appartenance = new ArrayList<>();

    @OneToMany(mappedBy = "femmeRurale")
    private List<RechercherParLocalisation>  rechhercherParLocalisation = new ArrayList<>();

    // Default constructor
    public FemmeRurale() {
        this.produits = new ArrayList<>();
        this.appartenance = new ArrayList<>();
        this.rechhercherParLocalisation = new ArrayList<>();
    }

    // Constructor with all fields
    public FemmeRurale(List<Produit> produits, List<Appartenance> appartenance, List<RechercherParLocalisation> rechhercherParLocalisation) {
        this.produits = produits != null ? produits : new ArrayList<>();
        this.appartenance = appartenance != null ? appartenance : new ArrayList<>();
        this.rechhercherParLocalisation = rechhercherParLocalisation != null ? rechhercherParLocalisation : new ArrayList<>();
    }

    // Getters and Setters
    public List<Produit> getProduits() {
        return produits;
    }

    public void setProduits(List<Produit> produits) {
        this.produits = produits;
    }

    public List<Appartenance> getAppartenance() {
        return appartenance;
    }

    public void setAppartenance(List<Appartenance> appartenance) {
        this.appartenance = appartenance;
    }

    public List<RechercherParLocalisation> getRechhercherParLocalisation() {
        return rechhercherParLocalisation;
    }

    public void setRechhercherParLocalisation(List<RechercherParLocalisation> rechhercherParLocalisation) {
        this.rechhercherParLocalisation = rechhercherParLocalisation;
    }

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FemmeRurale)) return false;
        if (!super.equals(o)) return false;
        FemmeRurale that = (FemmeRurale) o;
        return getId() != null && getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "FemmeRurale{" +
                "produits=" + produits +
                ", appartenance=" + appartenance +
                ", rechhercherParLocalisation=" + rechhercherParLocalisation +
                '}';
    }
}