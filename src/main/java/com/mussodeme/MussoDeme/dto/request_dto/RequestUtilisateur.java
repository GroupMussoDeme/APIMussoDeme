package com.mussodeme.MussoDeme.dto.request_dto;

import com.mussodeme.MussoDeme.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public abstract class RequestUtilisateur {
    private String nom;
    private String localite;
    private String numeroTel;
    private String motCle;
    private Role role;
}
