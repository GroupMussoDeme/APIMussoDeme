package com.mussodeme.MussoDeme.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mussodeme.MussoDeme.enums.Role;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminDTO {
    private Long id;
    private String nom;
    private Role role;
    private String email;
    @JsonIgnore
    private String motDePasse;

    // Default constructor
    public AdminDTO() {
    }

    // Constructor with all fields
    public AdminDTO(Long id, String nom, Role role, String email, String motDePasse) {
        this.id = id;
        this.nom = nom;
        this.role = role;
        this.email = email;
        this.motDePasse = motDePasse;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdminDTO)) return false;
        AdminDTO adminDTO = (AdminDTO) o;
        return id != null && id.equals(adminDTO.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "AdminDTO{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", role=" + role +
                ", email='" + email + '\'' +
                '}';
    }
}