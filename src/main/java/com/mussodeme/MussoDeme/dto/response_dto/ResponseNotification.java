package com.mussodeme.MussoDeme.dto.response_dto;

import com.mussodeme.MussoDeme.enums.TypeNotif;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor

public class ResponseNotification {
    private Long idNotification;
    private TypeNotif typeNotif;
    private String description;
    private  boolean status;
    private Date dateNotif;
}
