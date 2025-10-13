package com.mussodeme.MussoDeme.dto.request_dto;

import com.mussodeme.MussoDeme.enums.ModePaiement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class RequestPaiement {

    private LocalDateTime datePaiement;
    private ModePaiement modePaiement;
    private LocalDateTime datesPaiement;
    private Double montant;
}
