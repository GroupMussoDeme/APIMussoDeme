package com.mussodeme.MussoDeme.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "Identifiant (email ou numéro) requis")
    private String identifiant;

    @NotBlank(message = "Mot de passe requis")
    private String motDePasse;

    // Default constructor
    public LoginRequest() {
    }

    // Getters and Setters
    public @NotBlank(message = "Identifiant (email ou numéro) requis") String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(@NotBlank(message = "Identifiant (email ou numéro) requis") String identifiant) {
        this.identifiant = identifiant;
    }

    public @NotBlank(message = "Mot de passe requis") String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(@NotBlank(message = "Mot de passe requis") String motDePasse) {
        this.motDePasse = motDePasse;
    }

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoginRequest)) return false;
        LoginRequest that = (LoginRequest) o;
        return identifiant != null ? identifiant.equals(that.identifiant) : that.identifiant == null &&
               motDePasse != null ? motDePasse.equals(that.motDePasse) : that.motDePasse == null;
    }

    @Override
    public int hashCode() {
        int result = identifiant != null ? identifiant.hashCode() : 0;
        result = 31 * result + (motDePasse != null ? motDePasse.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "identifiant='" + identifiant + '\'' +
                ", motDePasse='" + motDePasse + '\'' +
                '}';
    }
}