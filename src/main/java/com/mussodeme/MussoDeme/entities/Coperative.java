package com.mussodeme.MussoDeme.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "coperative")
public class Coperative {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est requis")
    private String nom;

    private String description;

    private int nbrMembres;

    @OneToMany(mappedBy = "coperative", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appartenance> appartenance = new ArrayList<>();


    // Default constructor
    public Coperative() {
        this.appartenance = new ArrayList<>();
    }

    // Constructor with all fields
    public Coperative(Long id, String nom, String description, int nbrMembres, List<Appartenance> appartenance) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.nbrMembres = nbrMembres;
        this.appartenance = appartenance != null ? appartenance : new ArrayList<>();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotBlank(message = "Le nom est requis")
    public String getNom() {
        return nom;
    }

    public void setNom(@NotBlank(message = "Le nom est requis") String nom) {
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

    public List<Appartenance> getAppartenance() {
        return appartenance;
    }

    public void setAppartenance(List<Appartenance> appartenance) {
        this.appartenance = appartenance;
    }

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coperative)) return false;
        Coperative that = (Coperative) o;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Coperative{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", nbrMembres=" + nbrMembres +
                ", appartenance=" + appartenance +
                '}';
    }
}