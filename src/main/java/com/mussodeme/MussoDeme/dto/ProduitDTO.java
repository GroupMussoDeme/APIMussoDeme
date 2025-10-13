package com.mussodeme.MussoDeme.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class ProduitDTO {
    private Long id;
    private String nom;
    private String description;
    private String image;
    private Integer quantite;
    private Double prix;
    private Long femmeRuraleId;
}
