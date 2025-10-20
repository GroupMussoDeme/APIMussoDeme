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
public class ContenuDTO {
    private Long id;
    private String titre;
    private String langue;
    private String description;
    private String urlContenu;
    private String duree;

    private Long adminId;
    private Long categorieId;
}
