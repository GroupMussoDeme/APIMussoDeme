package com.mussodeme.MussoDeme.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

/**
 * Entité Acheteur - Représente un utilisateur qui achète des produits
 */
@Entity
@DiscriminatorValue("ACHETEUR")
public class Acheteur extends Utilisateur {
    
    @OneToMany(mappedBy = "acheteur")
    private List<RechercherParLocalisation> rechhercherParLocalisation = new ArrayList<>();

    // Default constructor
    public Acheteur() {
        this.rechhercherParLocalisation = new ArrayList<>();
    }

    // Constructor with all fields
    public Acheteur(List<RechercherParLocalisation> rechhercherParLocalisation) {
        this.rechhercherParLocalisation = rechhercherParLocalisation != null ? rechhercherParLocalisation : new ArrayList<>();
    }

    // Getters and Setters
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
        if (!(o instanceof Acheteur)) return false;
        if (!super.equals(o)) return false;
        Acheteur acheteur = (Acheteur) o;
        return getId() != null && getId().equals(acheteur.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Acheteur{" +
                "rechhercherParLocalisation=" + rechhercherParLocalisation +
                '}';
    }
}