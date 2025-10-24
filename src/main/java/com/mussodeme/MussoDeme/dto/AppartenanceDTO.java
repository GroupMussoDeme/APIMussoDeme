package com.mussodeme.MussoDeme.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppartenanceDTO {
    private Long id;
    private LocalDateTime dateIntegration;
    private Long coperativeId;
    private Long femmeRuraleId;

    // Default constructor
    public AppartenanceDTO() {
    }

    // Constructor with all fields
    public AppartenanceDTO(Long id, LocalDateTime dateIntegration, Long coperativeId, Long femmeRuraleId) {
        this.id = id;
        this.dateIntegration = dateIntegration;
        this.coperativeId = coperativeId;
        this.femmeRuraleId = femmeRuraleId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateIntegration() {
        return dateIntegration;
    }

    public void setDateIntegration(LocalDateTime dateIntegration) {
        this.dateIntegration = dateIntegration;
    }

    public Long getCoperativeId() {
        return coperativeId;
    }

    public void setCoperativeId(Long coperativeId) {
        this.coperativeId = coperativeId;
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
        if (!(o instanceof AppartenanceDTO)) return false;
        AppartenanceDTO that = (AppartenanceDTO) o;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "AppartenanceDTO{" +
                "id=" + id +
                ", dateIntegration=" + dateIntegration +
                ", coperativeId=" + coperativeId +
                ", femmeRuraleId=" + femmeRuraleId +
                '}';
    }
}