package com.mussodeme.MussoDeme.dto.request_dto;


import com.mussodeme.MussoDeme.enums.StatutCommande;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class RequestCommande {
    private Integer quantite;
    private StatutCommande statutCommande;
    private LocalDateTime dateAchat;
}
