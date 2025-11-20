package com.mussodeme.MussoDeme.entities;

import com.mussodeme.MussoDeme.enums.TypeNotif;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "notif_admin")
public class NotifAdmin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_notif")
    private TypeNotif typeNotif;

    private String description;
    private boolean status;
    private Date dateNotif;

    @ManyToOne
    @JoinColumn(name = "id_admin")
    private Admin admin;

    // Default constructor
    public NotifAdmin() {
    }

    // Constructor with all fields
    public NotifAdmin(Long id, TypeNotif typeNotif, String description, boolean status, Date dateNotif, Admin admin) {
        this.id = id;
        this.typeNotif = typeNotif;
        this.description = description;
        this.status = status;
        this.dateNotif = dateNotif;
        this.admin = admin;
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

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NotifAdmin)) return false;
        NotifAdmin that = (NotifAdmin) o;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "NotifAdmin{" +
                "id=" + id +
                ", typeNotif=" + typeNotif +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", dateNotif=" + dateNotif +
                ", admin=" + (admin != null ? admin.getId() : null) +
                '}';
    }
}