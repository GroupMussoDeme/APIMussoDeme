package com.mussodeme.MussoDeme.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RechercheParLocalisationDTO {
    private Long id;
    private Long acheteurId;
    private Long femmeRuraleId;

    // Default constructor
    public RechercheParLocalisationDTO() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAcheteurId() {
        return acheteurId;
    }

    public void setAcheteurId(Long acheteurId) {
        this.acheteurId = acheteurId;
    }

    public Long getFemmeRuraleId() {
        return femmeRuraleId;
    }

    public void setFemmeRuraleId(Long femmeRuraleId) {
        this.femmeRuraleId = femmeRuraleId;
    }

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RechercheParLocalisationDTO)) return false;
        RechercheParLocalisationDTO that = (RechercheParLocalisationDTO) o;
        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "RechercheParLocalisationDTO{" +
                "id=" + id +
                ", acheteurId=" + acheteurId +
                ", femmeRuraleId=" + femmeRuraleId +
                '}';
    }
}