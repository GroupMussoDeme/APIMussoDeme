package com.mussodeme.MussoDeme.security.service;

import com.mussodeme.MussoDeme.repository.AdminRepository;
import com.mussodeme.MussoDeme.repository.UtilisateursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service

public class CustomUserDetailsService implements UserDetailsService {

    private AdminRepository adminRepository;
    private UtilisateursRepository userRepository;

    @Autowired
    public CustomUserDetailsService(AdminRepository adminRepository, UtilisateursRepository userRepository) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Essaye d’abord de trouver un admin
        return adminRepository.findByEmail(username)
                .map(admin -> AuthUser.builder().admin(admin).build())
                .orElseGet(() -> userRepository.findFemmeRuraleByNumeroTel(username)
                        .map(user -> AuthUser.builder().utilisateur(user).build())
                        .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé")));
    }
}

