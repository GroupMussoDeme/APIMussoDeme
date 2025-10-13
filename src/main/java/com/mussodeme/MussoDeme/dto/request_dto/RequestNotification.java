package com.mussodeme.MussoDeme.dto.request_dto;

import com.mussodeme.MussoDeme.enums.TypeNotif;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class RequestNotification {

    private TypeNotif typeNotif;
    private String description;
    private  boolean status;
    private Date dateNotif;

}
