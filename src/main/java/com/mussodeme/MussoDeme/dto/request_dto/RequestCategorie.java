package com.mussodeme.MussoDeme.dto.request_dto;

import com.mussodeme.MussoDeme.enums.TypeCategorie;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class RequestCategorie {

    private String titre;
    private TypeCategorie typeCategorie;

}
