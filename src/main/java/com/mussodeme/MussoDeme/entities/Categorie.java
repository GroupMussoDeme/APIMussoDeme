package com.mussodeme.MussoDeme.entities;

import com.mussodeme.MussoDeme.enums.TypeCategorie;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "categorie")
public class Categorie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TypeCategorie typeCategorie;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;


    @OneToMany(mappedBy = "categorie", cascade = CascadeType.ALL)
    private List<Contenu> contenus;

    // Default constructor
    public Categorie() {
    }

    // Constructor with all fields
    public Categorie(Long id, TypeCategorie typeCategorie, Admin admin, List<Contenu> contenus) {
        this.id = id;
        this.typeCategorie = typeCategorie;
        this.admin = admin;
        this.contenus = contenus;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeCategorie getTypeCategorie() {
        return typeCategorie;
    }

    public void setTypeCategorie(TypeCategorie typeCategorie) {
        this.typeCategorie = typeCategorie;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public List<Contenu> getContenus() {
        return contenus;
    }

    public void setContenus(List<Contenu> contenus) {
        this.contenus = contenus;
    }

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Categorie)) return false;
        Categorie categorie = (Categorie) o;
        return id != null && id.equals(categorie.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Categorie{" +
                "id=" + id +
                ", typeCategorie=" + typeCategorie +
                ", admin=" + admin +
                ", contenus=" + contenus +
                '}';
    }
}