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

    // ------------------ LOGIN ------------------
    public Utilisateur authenticate(LoginRequest request) {

        if (request.getEmail() != null) {
            Admin admin = utilisateursRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new InvalidCredentialsException("Email ou mot de passe incorrect"));

            if (!passwordEncoder.matches(request.getSecret(), admin.getMotDePasse())) {
                throw new InvalidCredentialsException("Email ou mot de passe incorrect");
            }

            return admin;

        } else if (request.getNumeroTel() != null) {
            FemmeRurale femme = utilisateursRepository.findByNumeroTel(request.getNumeroTel())
                    .orElseThrow(() -> new InvalidCredentialsException("Numéro ou mot clé incorrect"));

            if (!(femme instanceof FemmeRurale)) {
                throw new InvalidCredentialsException("Cet utilisateur n'est pas une femme rurale");
            }

            if (!femme.getMotCle().equals(request.getSecret())) {
                throw new InvalidCredentialsException("Numéro ou mot clé incorrect");
            }

            return femme;

        } else {
            throw new InvalidCredentialsException("Email ou numéro requis pour la connexion");
        }
    }

    // ------------------ REGISTER ------------------
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

        } else if (request.getRole() == Role.FEMME_RURALE) {
            FemmeRurale fr = FemmeRurale.builder()
                    .nom(request.getNom())
                    .prenom(request.getPrenom())
                    .localite(request.getLocalite())
                    .numeroTel(request.getNumeroTel())
                    .motCle(request.getSecret())
                    .role(Role.FEMME_RURALE)
                    .build();

            return utilisateursRepository.save(fr);

        } else {
            throw new InvalidCredentialsException("Rôle non supporté pour l'inscription");
        }
    }
}
