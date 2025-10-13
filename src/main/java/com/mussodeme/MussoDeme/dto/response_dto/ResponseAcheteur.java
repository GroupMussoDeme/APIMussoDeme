package com.mussodeme.MussoDeme.dto.response_dto;

import com.mussodeme.MussoDeme.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ResponseAcheteur extends ResponseUtilisateur{

    public ResponseAcheteur(Long idUtilisateur, String nom, String localite, String numeroTel, Role role) {
        super(idUtilisateur, nom, localite, numeroTel, role);
    }
}
