package com.mussodeme.MussoDeme.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "gestion_admin")
public class GestionAdmin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "id_admin")
    private Admin admin;

    @ManyToOne
    @JoinColumn(nullable = false, name = "id_utilisateur")
    private Utilisateur utilisateur;

    // Default constructor
    public GestionAdmin() {
    }

    // Constructor with all fields
    public GestionAdmin(Long id, Admin admin, Utilisateur utilisateur) {
        this.id = id;
        this.admin = admin;
        this.utilisateur = utilisateur;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GestionAdmin)) return false;
        GestionAdmin that = (GestionAdmin) o;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "GestionAdmin{" +
                "id=" + id +
                ", admin=" + admin +
                ", utilisateur=" + utilisateur +
                '}';
    }
}