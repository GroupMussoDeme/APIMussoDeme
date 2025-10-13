package com.mussodeme.MussoDeme.dto.response_dto;

import com.mussodeme.MussoDeme.enums.TypeHistoriques;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor

public class ResponseHistorique {
    private Long idHistorique;
    private TypeHistoriques typeHistoriques;
    private LocalDateTime date;
}
