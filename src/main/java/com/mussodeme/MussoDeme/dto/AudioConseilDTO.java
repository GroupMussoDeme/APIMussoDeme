package com.mussodeme.MussoDeme.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class AudioConseilDTO {
    private Long id;
    private String titre;
    private String langue;
    private String description;
    private String imageUrl;
    private String urlAudio;
    private String duree;
    private Long categorieId;
    private Long adminId;
}
