package com.mussodeme.MussoDeme.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private String email;
    private String numeroTel;
    @NotBlank(message = "Le mot de passe ou mot clé est requis")
    private String secret;
}
