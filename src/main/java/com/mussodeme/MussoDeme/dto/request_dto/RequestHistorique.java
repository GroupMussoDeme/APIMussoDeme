package com.mussodeme.MussoDeme.dto.request_dto;

import com.mussodeme.MussoDeme.enums.TypeHistoriques;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class RequestHistorique {
    private TypeHistoriques typeHistoriques;
    private LocalDateTime date;
}
