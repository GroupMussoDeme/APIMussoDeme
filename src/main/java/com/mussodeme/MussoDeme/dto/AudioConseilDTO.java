package com.mussodeme.MussoDeme.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AudioConseilDTO {
    private Long id;
    private String titre;
    private String langue;
    private String description;
    private String urlAudio;
    private String duree;

    private Long utilisateurId;  // ✅ ancien adminId remplacé par utilisateurId
    private Long categorieId;    // ✅ lien vers la catégorie
}
