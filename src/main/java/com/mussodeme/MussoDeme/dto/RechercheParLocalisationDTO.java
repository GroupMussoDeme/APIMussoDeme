package com.mussodeme.MussoDeme.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RechercheParLocalisationDTO {
    private Long id;
    private Long acheteurId;
    private Long femmeRuraleId;
}
