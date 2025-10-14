package com.mussodeme.MussoDeme.security;

import com.mussodeme.MussoDeme.entities.Utilisateur;
import com.mussodeme.MussoDeme.exceptions.NotFoundException;
import com.mussodeme.MussoDeme.repository.UtilisateursRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UtilisateursRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Cherche d'abord un admin par email ou numéro de téléphone
        Utilisateur user = userRepository.findAdminByEmail(username)
                .<Utilisateur>map(u -> u)
                .or(() -> userRepository.findAdminByNumeroTel(username))
                .or(() -> userRepository.findFemmeRuraleByNumeroTel(username))
                .orElseThrow(() -> new NotFoundException("Utilisateur non trouvé avec ce username"));

        return AuthUser.builder()
                .user(user)
                .build();
    }
}
