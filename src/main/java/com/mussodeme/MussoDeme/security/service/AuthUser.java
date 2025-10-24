package com.mussodeme.MussoDeme.security.service;

import com.mussodeme.MussoDeme.entities.Admin;
import com.mussodeme.MussoDeme.entities.Utilisateur;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class AuthUser implements UserDetails {

    private Admin admin;
    private Utilisateur utilisateur;

    // Default constructor
    public AuthUser() {
    }

    // Getters and Setters
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (admin != null) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        if (utilisateur != null && utilisateur.getRole() != null) {
            return List.of(new SimpleGrantedAuthority("ROLE_" + utilisateur.getRole().name()));
        }
        return List.of();
    }

    @Override
    public String getPassword() {
        if (admin != null) return admin.getMotDePasse();
        return utilisateur != null ? utilisateur.getMotCle() : null;
    }

    @Override
    public String getUsername() {
        if (admin != null) return admin.getEmail();
        return utilisateur != null ? utilisateur.getNumeroTel() : null;
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthUser)) return false;
        AuthUser authUser = (AuthUser) o;
        return (admin != null ? admin.equals(authUser.admin) : authUser.admin == null) &&
               (utilisateur != null ? utilisateur.equals(authUser.utilisateur) : authUser.utilisateur == null);
    }

    @Override
    public int hashCode() {
        int result = admin != null ? admin.hashCode() : 0;
        result = 31 * result + (utilisateur != null ? utilisateur.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AuthUser{" +
                "admin=" + admin +
                ", utilisateur=" + utilisateur +
                '}';
    }
}