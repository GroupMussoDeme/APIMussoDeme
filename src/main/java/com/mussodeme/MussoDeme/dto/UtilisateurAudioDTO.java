package com.mussodeme.MussoDeme.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UtilisateurAudioDTO {
    private Long id;
    private Long utilisateurId;
    private Long audioConseilId;

    // Default constructor
    public UtilisateurAudioDTO() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public Long getAudioConseilId() {
        return audioConseilId;
    }

    public void setAudioConseilId(Long audioConseilId) {
        this.audioConseilId = audioConseilId;
    }

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UtilisateurAudioDTO)) return false;
        UtilisateurAudioDTO that = (UtilisateurAudioDTO) o;
        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UtilisateurAudioDTO{" +
                "id=" + id +
                ", utilisateurId=" + utilisateurId +
                ", audioConseilId=" + audioConseilId +
                '}';
    }
}