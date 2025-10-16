package com.mussodeme.MussoDeme.services;

import com.mussodeme.MussoDeme.dto.LoginRequest;
import com.mussodeme.MussoDeme.dto.RegisterRequest;
import com.mussodeme.MussoDeme.entities.Admin;
import com.mussodeme.MussoDeme.entities.FemmeRurale;
import com.mussodeme.MussoDeme.entities.Utilisateur;
import com.mussodeme.MussoDeme.enums.Role;
import com.mussodeme.MussoDeme.exceptions.InvalidCredentialsException;
import com.mussodeme.MussoDeme.repository.UtilisateursRepository;
import com.mussodeme.MussoDeme.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UtilisateursRepository utilisateursRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    // ------------------ AUTHENTIFICATION ------------------
    public Utilisateur authenticate(LoginRequest request) {

        // 🟢 Si l'utilisateur se connecte avec un email (Admin)
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            Admin admin = utilisateursRepository.findAdminByEmail(request.getEmail())
                    .orElseThrow(() -> new InvalidCredentialsException("Email ou mot de passe incorrect"));

            if (!passwordEncoder.matches(request.getSecret(), admin.getMotDePasse())) {
                throw new InvalidCredentialsException("Email ou mot de passe incorrect");
            }

            return admin;
        }

        // 🟢 Si l'utilisateur se connecte avec un numéro (Admin ou Femme Rurale)
        else if (request.getNumeroTel() != null && !request.getNumeroTel().isEmpty()) {

            // D’abord, on regarde si c’est un admin
            Admin admin = utilisateursRepository.findAdminByNumeroTel(request.getNumeroTel()).orElse(null);
            if (admin != null) {
                if (!passwordEncoder.matches(request.getSecret(), admin.getMotDePasse())) {
                    throw new InvalidCredentialsException("Mot de passe incorrect pour l’administrateur");
                }
                return admin;
            }

            // Sinon, c’est peut-être une femme rurale
            FemmeRurale femme = utilisateursRepository.findFemmeRuraleByNumeroTel(request.getNumeroTel())
                    .orElseThrow(() -> new InvalidCredentialsException("Numéro ou mot clé incorrect"));

            if (!femme.getMotCle().equals(request.getSecret())) {
                throw new InvalidCredentialsException("Numéro ou mot clé incorrect");
            }

            return femme;
        }

        else {
            throw new InvalidCredentialsException("Email ou numéro requis pour la connexion");
        }
    }

    // ------------------ INSCRIPTION ------------------
    public Utilisateur register(RegisterRequest request) {

        if (request.getRole() == Role.ADMIN) {
            Admin admin = Admin.builder()
                    .nom(request.getNom())
                    .localite(request.getLocalite())
                    .numeroTel(request.getNumeroTel())
                    .email(request.getEmail())
                    .motDePasse(passwordEncoder.encode(request.getSecret()))
                    .role(Role.ADMIN)
                    .build();

            return utilisateursRepository.save(admin);
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
