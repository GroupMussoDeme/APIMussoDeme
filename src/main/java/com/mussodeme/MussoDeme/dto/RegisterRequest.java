package com.mussodeme.MussoDeme.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String prenom;
    private String localite;
    private String numeroTel;
    @JsonIgnore
    private String motCle;
    private Role role;

}
