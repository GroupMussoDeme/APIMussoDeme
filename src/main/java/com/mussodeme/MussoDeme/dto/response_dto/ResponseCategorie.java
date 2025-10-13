package com.mussodeme.MussoDeme.dto.response_dto;

import com.mussodeme.MussoDeme.enums.TypeCategorie;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class ResponseCategorie {
    private Long idCategorie;
    private String titre;
    private TypeCategorie typeCategorie;
}
