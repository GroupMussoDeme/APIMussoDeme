package com.mussodeme.MussoDeme.controllers;

import com.mussodeme.MussoDeme.dto.*;
import com.mussodeme.MussoDeme.entities.Admin;
import com.mussodeme.MussoDeme.entities.FemmeRurale;
import com.mussodeme.MussoDeme.entities.Utilisateur;
import com.mussodeme.MussoDeme.security.util.JwtUtils;
import com.mussodeme.MussoDeme.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<Response> login(@Valid @RequestBody LoginRequest request) {
        Utilisateur utilisateur =  (Utilisateur) authService.authenticate(request);
        Response response;

        if (utilisateur instanceof Admin) {
            UtilisateurDTO dto = new UtilisateurDTO();
            dto.setId(admin.getId());
            dto.setNom(admin.getNom());
            dto.setLocalite(admin.getLocalite());
            dto.setNumeroTel(admin.getNumeroTel());
            dto.setRole(admin.getRole());

            String token = jwtUtils.generateToken(admin.getEmail());

            response = Response.builder()
                    .status(200)
                    .message("Connexion réussie")
                    .token(token)
                    .role(admin.getRole().name())
                    .utilisateur(dto)
                    .build();

        } else if (utilisateur instanceof FemmeRurale fr) {
            FemmeRuraleDTO dto = new FemmeRuraleDTO();
            dto.setId(fr.getId());
            dto.setNom(fr.getNom());
            dto.setPrenom(fr.getPrenom());
            dto.setLocalite(fr.getLocalite());
            dto.setNumeroTel(fr.getNumeroTel());
            dto.setRole(fr.getRole());

            String token = jwtUtils.generateToken(fr.getNumeroTel());

            response = Response.builder()
                    .status(200)
                    .message("Connexion réussie")
                    .token(token)
                    .role(fr.getRole().name())
                    .femmeRurale(dto)
                    .build();
        } else {
            response = Response.builder()
                    .status(400)
                    .message("Type d'utilisateur inconnu")
                    .build();
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Response> register(@Valid @RequestBody RegisterRequest request) {
        Utilisateur utilisateur = authService.register(request);
        Response response;

        if (utilisateur instanceof Admin admin) {
            UtilisateurDTO dto = new UtilisateurDTO();
            dto.setId(admin.getId());
            dto.setNom(admin.getNom());
            dto.setLocalite(admin.getLocalite());
            dto.setNumeroTel(admin.getNumeroTel());
            dto.setRole(admin.getRole());

            response = Response.builder()
                    .status(201)
                    .message("Admin enregistré avec succès")
                    .utilisateur(dto)
                    .build();

        } else if (utilisateur instanceof FemmeRurale fr) {
            FemmeRuraleDTO dto = new FemmeRuraleDTO();
            dto.setId(fr.getId());
            dto.setNom(fr.getNom());
            dto.setPrenom(fr.getPrenom());
            dto.setLocalite(fr.getLocalite());
            dto.setNumeroTel(fr.getNumeroTel());
            dto.setRole(fr.getRole());

            response = Response.builder()
                    .status(201)
                    .message("Femme rurale enregistrée avec succès")
                    .femmeRurale(dto)
                    .build();
        } else {
            response = Response.builder()
                    .status(400)
                    .message("Type d'utilisateur inconnu")
                    .build();
        }

        return ResponseEntity.ok(response);
    }
}
