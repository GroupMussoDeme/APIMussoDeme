package com.mussodeme.MussoDeme.dto.response_dto;

import com.mussodeme.MussoDeme.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ResponseInstitutionFinanciere extends ResponseUtilisateur{
    public ResponseInstitutionFinanciere(Long idUtilisateur, String nom, String localite, String numeroTel, Role role) {
        super(idUtilisateur, nom, localite, numeroTel, role);
    }
}
