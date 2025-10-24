package com.mussodeme.MussoDeme.dto;

import jakarta.validation.constraints.NotBlank;

public class RegisterRequest {

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    private String prenom;

    @NotBlank(message = "La localité est obligatoire")
    private String localite;

    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    private String numeroTel;

    @NotBlank(message = "Le mot clé est obligatoire")
    private String motCle;

    private boolean active;

    // Default constructor
    public RegisterRequest() {
    }

    // Getters and Setters
    public @NotBlank(message = "Le nom est obligatoire") String getNom() {
        return nom;
    }

    public void setNom(@NotBlank(message = "Le nom est obligatoire") String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public @NotBlank(message = "La localité est obligatoire") String getLocalite() {
        return localite;
    }

    public void setLocalite(@NotBlank(message = "La localité est obligatoire") String localite) {
        this.localite = localite;
    }

    public @NotBlank(message = "Le numéro de téléphone est obligatoire") String getNumeroTel() {
        return numeroTel;
    }

    public void setNumeroTel(@NotBlank(message = "Le numéro de téléphone est obligatoire") String numeroTel) {
        this.numeroTel = numeroTel;
    }

    public @NotBlank(message = "Le mot clé est obligatoire") String getMotCle() {
        return motCle;
    }

    public void setMotCle(@NotBlank(message = "Le mot clé est obligatoire") String motCle) {
        this.motCle = motCle;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegisterRequest)) return false;
        RegisterRequest that = (RegisterRequest) o;
        return active == that.active &&
               nom != null ? nom.equals(that.nom) : that.nom == null &&
               prenom != null ? prenom.equals(that.prenom) : that.prenom == null &&
               localite != null ? localite.equals(that.localite) : that.localite == null &&
               numeroTel != null ? numeroTel.equals(that.numeroTel) : that.numeroTel == null &&
               motCle != null ? motCle.equals(that.motCle) : that.motCle == null;
    }

    @Override
    public int hashCode() {
        int result = nom != null ? nom.hashCode() : 0;
        result = 31 * result + (prenom != null ? prenom.hashCode() : 0);
        result = 31 * result + (localite != null ? localite.hashCode() : 0);
        result = 31 * result + (numeroTel != null ? numeroTel.hashCode() : 0);
        result = 31 * result + (motCle != null ? motCle.hashCode() : 0);
        result = 31 * result + (active ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RegisterRequest{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", localite='" + localite + '\'' +
                ", numeroTel='" + numeroTel + '\'' +
                ", motCle='" + motCle + '\'' +
                ", active=" + active +
                '}';
    }
}