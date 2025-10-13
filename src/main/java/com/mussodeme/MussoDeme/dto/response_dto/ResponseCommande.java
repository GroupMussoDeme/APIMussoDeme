package com.mussodeme.MussoDeme.dto.response_dto;

import com.mussodeme.MussoDeme.enums.StatutCommande;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor

public class ResponseCommande {
    private Long idCommande;
    private Integer quantite;
    private StatutCommande statutCommande;
    private LocalDateTime dateAchat;

}
