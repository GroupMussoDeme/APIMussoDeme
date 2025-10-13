package com.mussodeme.MussoDeme.dto.response_dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class ResponseCoperative {
    private Long idCoperative;
    private String nom;
    private String description;
    private int nbrMembres;
}
