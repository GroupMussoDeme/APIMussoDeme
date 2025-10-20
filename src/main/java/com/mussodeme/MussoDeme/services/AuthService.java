package com.mussodeme.MussoDeme.services;

import com.mussodeme.MussoDeme.dto.FemmeRuraleDTO;
import com.mussodeme.MussoDeme.dto.LoginRequest;
import com.mussodeme.MussoDeme.dto.LoginResponse;
import com.mussodeme.MussoDeme.dto.RegisterRequest;
import com.mussodeme.MussoDeme.entities.FemmeRurale;
import com.mussodeme.MussoDeme.enums.Role;
import com.mussodeme.MussoDeme.repository.AdminRepository;
import com.mussodeme.MussoDeme.repository.FemmeRuraleRepository;
import com.mussodeme.MussoDeme.security.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AdminRepository adminRepository;
    private final FemmeRuraleRepository femmeRuraleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    /**
     * Connexion (Admin par email / Femme Rurale par numéro)
     */
    public LoginResponse login(LoginRequest request) {
        String identifiant = request.getIdentifiant();
        String motDePasse = request.getMotDePasse();

        if (identifiant == null || motDePasse == null) {
            throw new BadCredentialsException("Identifiant ou mot de passe manquant !");
        }

        boolean isEmail = identifiant.contains("@");

        if (isEmail) {
            //Connexion admin
            var admin = adminRepository.findByEmail(identifiant)
                    .orElseThrow(() -> new BadCredentialsException("Email ou mot de passe invalide"));

            if (!passwordEncoder.matches(motDePasse, admin.getMotDePasse())) {
                throw new BadCredentialsException("Mot de passe incorrect");
            }

            String token = jwtUtils.generateToken(admin.getEmail());
            return LoginResponse.builder()
                    .token(token)
                    .username(admin.getEmail())
                    .role("ROLE_ADMIN")
                    .build();

        } else {
            //Connexion femme rurale
            var femme = femmeRuraleRepository.findByNumeroTel(identifiant)
                    .orElseThrow(() -> new BadCredentialsException("Numéro ou mot clé invalide"));

            if (!passwordEncoder.matches(motDePasse, femme.getMotCle())) {
                throw new BadCredentialsException("Mot clé incorrect");
            }

            String token = jwtUtils.generateToken(femme.getNumeroTel());
            return LoginResponse.builder()
                    .token(token)
                    .username(femme.getNumeroTel())
                    .role("ROLE_FEMME_RURALE")
                    .build();
        }
    }

    /**
     * Inscription Femme Rurale
     */
    public FemmeRuraleDTO registerFemmeRurale(RegisterRequest request) {
        if (femmeRuraleRepository.existsByNumeroTel(request.getNumeroTel())) {
            throw new RuntimeException("Ce numéro est déjà utilisé !");
        }

        FemmeRurale femme = new FemmeRurale();
        femme.setNom(request.getNom());
        femme.setPrenom(request.getPrenom());
        femme.setNumeroTel(request.getNumeroTel());
        femme.setLocalite(request.getLocalite());
        femme.setMotCle(passwordEncoder.encode(request.getMotCle()));
        femme.setRole(Role.FEMME_RURALE);
        femme.setActive(true);

        FemmeRurale saved = femmeRuraleRepository.save(femme);

        return new FemmeRuraleDTO(
                saved.getId(),
                saved.getNom(),
                saved.getPrenom(),
                saved.getLocalite(),
                saved.getNumeroTel(),
                saved.getMotCle(),
                saved.getRole(),
                saved.isActive()
        );
    }
}
