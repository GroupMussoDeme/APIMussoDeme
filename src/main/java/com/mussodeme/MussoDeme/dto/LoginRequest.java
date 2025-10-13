package com.mussodeme.MussoDeme.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Numero tel is required")
    private String numeroTel;
    @NotBlank(message = "Mot cle is required")
    private String motCle;

}
