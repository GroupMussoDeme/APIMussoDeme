package com.mussodeme.MussoDeme.services;

import com.mussodeme.MussoDeme.dto.LoginRequest;
import com.mussodeme.MussoDeme.dto.RegisterRequest;
import com.mussodeme.MussoDeme.entities.Admin;
import com.mussodeme.MussoDeme.entities.FemmeRurale;
import com.mussodeme.MussoDeme.entities.Utilisateur;
import com.mussodeme.MussoDeme.enums.Role;
import com.mussodeme.MussoDeme.exceptions.InvalidCredentialsException;
import com.mussodeme.MussoDeme.repository.AdminRepository;
import com.mussodeme.MussoDeme.repository.FemmeRuraleRepository;
import com.mussodeme.MussoDeme.repository.UtilisateursRepository;
import com.mussodeme.MussoDeme.security.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AdminRepository adminRepository;
    private final FemmeRuraleRepository femmeRuraleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UtilisateursRepository utilisateursRepository;

    // ------------------ AUTHENTIFICATION ------------------
    public Object authenticate(LoginRequest request) {

        // 🟢 Si c’est un admin (email ou numéro)
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            Admin admin = adminRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new InvalidCredentialsException("Email ou mot de passe incorrect"));

            if (!passwordEncoder.matches(request.getSecret(), admin.getMotDePasse())) {
                throw new InvalidCredentialsException("Email ou mot de passe incorrect");
            }
            return admin;
        }

        if (request.getNumeroTel() != null && !request.getNumeroTel().isEmpty()) {
            // Vérifie si c’est un admin
            Admin admin = adminRepository.findByNumeroTel(request.getNumeroTel()).orElse(null);
            if (admin != null) {
                if (!passwordEncoder.matches(request.getSecret(), admin.getMotDePasse())) {
                    throw new InvalidCredentialsException("Mot de passe incorrect pour l’administrateur");
                }
                return admin;
            }

            // Sinon, c’est une femme rurale
            FemmeRurale femme = femmeRuraleRepository.findByNumeroTel(request.getNumeroTel())
                    .orElseThrow(() -> new InvalidCredentialsException("Numéro ou mot clé incorrect"));

            if (!femme.getMotCle().equals(request.getSecret())) {
                throw new InvalidCredentialsException("Numéro ou mot clé incorrect");
            }

            return femme;
        }

        throw new InvalidCredentialsException("Email ou numéro requis pour la connexion");
    }

    public Utilisateur register(RegisterRequest request) {

        if (request.getRole() == Role.ADMIN) {
            throw new InvalidCredentialsException("L'administrateur est déjà configuré. Impossible de créer un nouvel admin.");
        }

        else if (request.getRole() == Role.FEMME_RURALE) {
            FemmeRurale femme = FemmeRurale.builder()
                    .nom(request.getNom())
                    .prenom(request.getPrenom())
                    .localite(request.getLocalite())
                    .numeroTel(request.getNumeroTel())
                    .motCle(request.getSecret())
                    .role(Role.FEMME_RURALE)
                    .build();

            return utilisateursRepository.save(femme);
        }

        else {
            throw new InvalidCredentialsException("Rôle non supporté pour l'inscription");
        }
    }

}
