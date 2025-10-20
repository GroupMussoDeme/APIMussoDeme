package com.mussodeme.MussoDeme.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    private String prenom;

    @NotBlank(message = "La localité est obligatoire")
    private String localite;

    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    private String numeroTel;

    @NotBlank(message = "Le mot clé est obligatoire")
    private String motCle;

    private boolean active;
}
