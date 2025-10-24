package com.mussodeme.MussoDeme.entities;

import com.mussodeme.MussoDeme.enums.Role;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "admin")

public class Admin  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String motDePasse;

    private boolean active;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "admin")
    private List<Contenu> contenus;

    @OneToMany(mappedBy = "admin")
    private List<Categorie> categories;

    @OneToMany(mappedBy = "admin")
    private List<GestionAdmin> gestionsAdmin = new ArrayList<>();

    // Default constructor
    public Admin() {
        this.contenus = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.gestionsAdmin = new ArrayList<>();
    }

    // Constructor with all fields
    public Admin(Long id, String nom, String email, String motDePasse, boolean active, Role role, 
                 List<Contenu> contenus, List<Categorie> categories, List<GestionAdmin> gestionsAdmin) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.active = active;
        this.role = role;
        this.contenus = contenus != null ? contenus : new ArrayList<>();
        this.categories = categories != null ? categories : new ArrayList<>();
        this.gestionsAdmin = gestionsAdmin != null ? gestionsAdmin : new ArrayList<>();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Contenu> getContenus() {
        return contenus;
    }

    public void setContenus(List<Contenu> contenus) {
        this.contenus = contenus;
    }

    public List<Categorie> getCategories() {
        return categories;
    }

    public void setCategories(List<Categorie> categories) {
        this.categories = categories;
    }

    public List<GestionAdmin> getGestionsAdmin() {
        return gestionsAdmin;
    }

    public void setGestionsAdmin(List<GestionAdmin> gestionsAdmin) {
        this.gestionsAdmin = gestionsAdmin;
    }

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Admin)) return false;
        Admin admin = (Admin) o;
        return id != null && id.equals(admin.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", active=" + active +
                ", role=" + role +
                '}';
    }
}