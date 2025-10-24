package com.mussodeme.MussoDeme.dto;

import com.mussodeme.MussoDeme.enums.TypeNotif;

import java.util.Date;

public class NotificationDTO {
    private Long id;
    private TypeNotif typeNotif;
    private String description;
    private boolean status; // false = non lue, true = lue
    private Date dateNotif;
    private Long utilisateurId;
    private String utilisateurNom;

    // Default constructor
    public NotificationDTO() {
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

    public Long getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public String getUtilisateurNom() {
        return utilisateurNom;
    }

    public void setUtilisateurNom(String utilisateurNom) {
        this.utilisateurNom = utilisateurNom;
    }

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NotificationDTO)) return false;
        NotificationDTO that = (NotificationDTO) o;
        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "NotificationDTO{" +
                "id=" + id +
                ", typeNotif=" + typeNotif +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", dateNotif=" + dateNotif +
                ", utilisateurId=" + utilisateurId +
                ", utilisateurNom='" + utilisateurNom + '\'' +
                '}';
    }
}