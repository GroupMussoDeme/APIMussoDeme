package com.mussodeme.MussoDeme.dto;

import com.mussodeme.MussoDeme.enums.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String nom;
    private String prenom;       // utilisé uniquement pour FemmeRurale
    private String localite;
    private String numeroTel;
    private String email;        // utilisé uniquement pour Admin
    @NotBlank(message = "Le mot de passe ou mot clé est requis")
    private String secret;       // motDePasse pour Admin, motCle pour FemmeRurale
    private Role role;
    private boolean active;
}
