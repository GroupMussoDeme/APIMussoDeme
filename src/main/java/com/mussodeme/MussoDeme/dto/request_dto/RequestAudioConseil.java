package com.mussodeme.MussoDeme.dto.request_dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class RequestAudioConseil {
    private String titre;
    private String langue;
    private String description;
    private String imageUrl;
    private String duree;
}
