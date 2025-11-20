package com.mussodeme.MussoDeme.dto;

import com.mussodeme.MussoDeme.enums.TypeNotif;

import java.util.Date;

public class NotifAdminDTO {
    private Long id;
    private TypeNotif typeNotif;
    private String description;
    private boolean status;
    private Date dateNotif;
    private Long adminId;
    private String adminNom;

    // Default constructor
    public NotifAdminDTO() {
    }

    // Constructor with all fields
    public NotifAdminDTO(Long id, TypeNotif typeNotif, String description, boolean status, Date dateNotif, Long adminId, String adminNom) {
        this.id = id;
        this.typeNotif = typeNotif;
        this.description = description;
        this.status = status;
        this.dateNotif = dateNotif;
        this.adminId = adminId;
        this.adminNom = adminNom;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeNotif getTypeNotif() {
        return typeNotif;
    }

    public void setTypeNotif(TypeNotif typeNotif) {
        this.typeNotif = typeNotif;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getDateNotif() {
        return dateNotif;
    }

    public void setDateNotif(Date dateNotif) {
        this.dateNotif = dateNotif;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getAdminNom() {
        return adminNom;
    }

    public void setAdminNom(String adminNom) {
        this.adminNom = adminNom;
    }

    @Override
    public String toString() {
        return "NotifAdminDTO{" +
                "id=" + id +
                ", typeNotif=" + typeNotif +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", dateNotif=" + dateNotif +
                ", adminId=" + adminId +
                ", adminNom='" + adminNom + '\'' +
                '}';
    }
}