package com.mussodeme.MussoDeme.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mussodeme.MussoDeme.enums.TypeHistoriques;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class HistoriqueDTO {
    private Long id;
    private TypeHistoriques typeHistoriques;
    private LocalDateTime date;
    private Long utilisateurId;
}
