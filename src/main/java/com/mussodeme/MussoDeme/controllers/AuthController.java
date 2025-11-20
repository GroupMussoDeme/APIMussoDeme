package com.mussodeme.MussoDeme.controllers;

import com.mussodeme.MussoDeme.dto.FemmeRuraleDTO;
import com.mussodeme.MussoDeme.dto.LoginRequest;
import com.mussodeme.MussoDeme.dto.LoginResponse;
import com.mussodeme.MussoDeme.dto.RefreshTokenRequest;
import com.mussodeme.MussoDeme.dto.RegisterRequest;
import com.mussodeme.MussoDeme.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private static final Logger logger = Logger.getLogger(AuthController.class.getName());

    private final AuthService authService;

    // Constructor for dependency injection
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<FemmeRuraleDTO> register(@Valid @RequestBody RegisterRequest request) {
        logger.info("Requête d'inscription reçue pour le numéro: " + request.getNumeroTel());
        FemmeRuraleDTO response = authService.registerFemmeRurale(request);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        logger.info("Requête de connexion reçue");
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh/refresh")
    public ResponseEntity<LoginResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        logger.info("Requête de rafraîchissement de token reçue");
        LoginResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }
}