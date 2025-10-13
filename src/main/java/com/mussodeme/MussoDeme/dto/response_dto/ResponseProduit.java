package com.mussodeme.MussoDeme.dto.response_dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class ResponseProduit {
    private Long idProduit;
    private String nom;
    private String description;
    private String image;
    private Integer quantite;
    private Double prix;
}
