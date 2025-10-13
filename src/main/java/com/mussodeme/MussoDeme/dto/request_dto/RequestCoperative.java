package com.mussodeme.MussoDeme.dto.request_dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class RequestCoperative {

    private String nom;
    private String description;
    private int nbrMembres;
}
