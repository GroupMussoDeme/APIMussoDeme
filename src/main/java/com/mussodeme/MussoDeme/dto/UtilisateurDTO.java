package com.mussodeme.MussoDeme.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mussodeme.MussoDeme.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class UtilisateurDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String localite;
    private String numeroTel;
    @JsonIgnore
    private String motCle;
    private Role role;
}
