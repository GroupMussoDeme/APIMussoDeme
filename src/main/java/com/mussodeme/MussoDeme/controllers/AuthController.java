package com.mussodeme.MussoDeme.controllers;

import com.mussodeme.MussoDeme.dto.FemmeRuraleDTO;
import com.mussodeme.MussoDeme.dto.LoginRequest;
import com.mussodeme.MussoDeme.dto.LoginResponse;
import com.mussodeme.MussoDeme.dto.RegisterRequest;
import com.mussodeme.MussoDeme.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<FemmeRuraleDTO> register(@RequestBody RegisterRequest request) {
        FemmeRuraleDTO response = authService.registerFemmeRurale(request);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
