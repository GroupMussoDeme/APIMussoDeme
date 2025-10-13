package com.mussodeme.MussoDeme.dto.response_dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class ResponseTutos {
    private Long idTutos;
    private String titre;
    private String langue;
    private String description;
    private String urlVideao;
    private String duree;
}
