package com.mussodeme.MussoDeme.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mussodeme.MussoDeme.enums.ModePaiement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaiementDTO {
    private Long id;
    private LocalDateTime datePaiement;
    private ModePaiement modePaiement;
    private Double montant;
    private Long acheteurId;
}
