package com.mussodeme.MussoDeme.dto;

import com.mussodeme.MussoDeme.enums.Role;

public class CreateUtilisateurRequest {
    private String nom;
    private String prenom;
    private String numeroTel;
    private String motCle;
    private String localite;
    private String email;
    private Role role;
    private boolean active;

    // Default constructor
    public CreateUtilisateurRequest() {
    }

    // Constructor with all fields
    public CreateUtilisateurRequest(String nom, String prenom, String numeroTel, String motCle, 
                                  String localite, String email, Role role, boolean active) {
        this.nom = nom;
        this.prenom = prenom;
        this.numeroTel = numeroTel;
        this.motCle = motCle;
        this.localite = localite;
        this.email = email;
        this.role = role;
        this.active = active;
    }

    // Getters and Setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNumeroTel() {
        return numeroTel;
    }

    public void setNumeroTel(String numeroTel) {
        this.numeroTel = numeroTel;
    }

    public String getMotCle() {
        return motCle;
    }

    public void setMotCle(String motCle) {
        this.motCle = motCle;
    }

    public String getLocalite() {
        return localite;
    }

    public void setLocalite(String localite) {
        this.localite = localite;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}