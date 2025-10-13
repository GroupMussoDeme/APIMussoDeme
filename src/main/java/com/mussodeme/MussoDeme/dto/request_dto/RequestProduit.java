package com.mussodeme.MussoDeme.dto.request_dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class RequestProduit {
    private String nom;
    private String description;
    private String image;
    private Integer quantite;
    private Double prix;
}
