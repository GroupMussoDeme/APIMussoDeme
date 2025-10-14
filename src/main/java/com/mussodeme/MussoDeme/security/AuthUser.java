package com.mussodeme.MussoDeme.security;

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

    private Utilisateur user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    @Override
    public String getPassword() {
        if (user instanceof com.mussodeme.MussoDeme.entities.Admin admin) {
            return admin.getMotDePasse();
        }
        // Pour les femmes rurales, motCle n'est pas utilisé par Spring Security pour login standard
        return user.getMotCle();
    }

    @Override
    public String getUsername() {
        if (user instanceof com.mussodeme.MussoDeme.entities.Admin admin) {
            return admin.getEmail();
        }
        return user.getNumeroTel();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
