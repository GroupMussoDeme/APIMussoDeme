package com.mussodeme.MussoDeme.security.service;

import com.mussodeme.MussoDeme.entities.Admin;
import com.mussodeme.MussoDeme.entities.FemmeRurale;
import com.mussodeme.MussoDeme.repository.AdminRepository;
import com.mussodeme.MussoDeme.repository.FemmeRuraleRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final FemmeRuraleRepository femmeRuraleRepository;

    // Constructor for dependency injection
    public CustomUserDetailsService(AdminRepository adminRepository, FemmeRuraleRepository femmeRuraleRepository) {
        this.adminRepository = adminRepository;
        this.femmeRuraleRepository = femmeRuraleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {

        // Recherche d’un admin par email
        Admin admin = adminRepository.findByEmail(identifier).orElse(null);
        if (admin != null) {
            AuthUser authUser = new AuthUser();
            authUser.setAdmin(admin);
            return authUser;
        }

        // Sinon, recherche d’une femme rurale par numéro
        FemmeRurale femme = femmeRuraleRepository.findByNumeroTel(identifier).orElse(null);
        if (femme != null) {
            AuthUser authUser = new AuthUser();
            authUser.setUtilisateur(femme);
            return authUser;
        }

        throw new UsernameNotFoundException("Utilisateur non trouvé : " + identifier);
    }
}