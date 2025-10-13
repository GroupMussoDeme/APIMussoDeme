package com.mussodeme.MussoDeme.dto.response_dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class ResponseAudioConseil {
    private String titre;
    private String langue;
    private String description;
    private String imageUrl;
    private String duree;
}
