package com.mussodeme.MussoDeme.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mussodeme.MussoDeme.enums.TypeNotif;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationDTO {
    private Long id;
    private TypeNotif typeNotif;
    private String description;
    private boolean status;
    private Date dateNotif;
    private Long utilisateurId;

}
