package com.mussodeme.MussoDeme.security.service;

import com.mussodeme.MussoDeme.entities.Admin;
import com.mussodeme.MussoDeme.entities.Utilisateur;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
public class AuthUser implements UserDetails {

    private Admin admin;
    private Utilisateur utilisateur;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (admin != null)
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return List.of(new SimpleGrantedAuthority("ROLE_" + utilisateur.getRole().name()));
    }

    @Override
    public String getPassword() {
        if (admin != null) return admin.getMotDePasse();
        return utilisateur.getMotCle();
    }

    @Override
    public String getUsername() {
        if (admin != null) return admin.getEmail();
        return utilisateur.getNumeroTel();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
