package com.mussodeme.MussoDeme.dto.response_dto;

import com.mussodeme.MussoDeme.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class ResponseUtilisateur {
    private Long idUtilisateur;
    private String nom;
    private String localite;
    private String numeroTel;
    private Role role;
}
