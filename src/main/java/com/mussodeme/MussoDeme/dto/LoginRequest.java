package com.mussodeme.MussoDeme.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Identifiant (email ou numéro) requis")
    private String identifiant;

    @NotBlank(message = "Mot de passe requis")
    private String motDePasse;
}
