package com.mussodeme.MussoDeme.dto.response_dto;

import com.mussodeme.MussoDeme.enums.ModePaiement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor

public class ResponsePaiement {
    private long idPaiement;
    private LocalDateTime datePaiement;
    private ModePaiement modePaiement;
    private LocalDateTime datesPaiement;
    private Double montant;
}
