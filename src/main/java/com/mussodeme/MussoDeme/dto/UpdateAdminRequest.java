package com.mussodeme.MussoDeme.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UpdateAdminRequest {
    
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    private String nom;
    
    @Email(message = "Format d'email invalide")
    private String email;
    
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String ancienMotDePasse;
    
    @Size(min = 8, message = "Le nouveau mot de passe doit contenir au moins 8 caractères")
    private String nouveauMotDePasse;

    // Default constructor
    public UpdateAdminRequest() {
    }

    // Constructor with all fields
    public UpdateAdminRequest(String nom, String email, String ancienMotDePasse, String nouveauMotDePasse) {
        this.nom = nom;
        this.email = email;
        this.ancienMotDePasse = ancienMotDePasse;
        this.nouveauMotDePasse = nouveauMotDePasse;
    }

    // Getters and Setters
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    public String getNom() {
        return nom;
    }

    public void setNom(@Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères") String nom) {
        this.nom = nom;
    }

    @Email(message = "Format d'email invalide")
    public String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "Format d'email invalide") String email) {
        this.email = email;
    }

    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    public String getAncienMotDePasse() {
        return ancienMotDePasse;
    }

    public void setAncienMotDePasse(@Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères") String ancienMotDePasse) {
        this.ancienMotDePasse = ancienMotDePasse;
    }

    @Size(min = 8, message = "Le nouveau mot de passe doit contenir au moins 8 caractères")
    public String getNouveauMotDePasse() {
        return nouveauMotDePasse;
    }

    public void setNouveauMotDePasse(@Size(min = 8, message = "Le nouveau mot de passe doit contenir au moins 8 caractères") String nouveauMotDePasse) {
        this.nouveauMotDePasse = nouveauMotDePasse;
    }

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UpdateAdminRequest)) return false;
        UpdateAdminRequest that = (UpdateAdminRequest) o;
        return nom != null ? nom.equals(that.nom) : that.nom == null &&
               email != null ? email.equals(that.email) : that.email == null;
    }

    @Override
    public int hashCode() {
        int result = nom != null ? nom.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UpdateAdminRequest{" +
                "nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", ancienMotDePasse='" + ancienMotDePasse + '\'' +
                ", nouveauMotDePasse='" + nouveauMotDePasse + '\'' +
                '}';
    }
}