package com.mussodeme.MussoDeme.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "utilisateur_audio")
public class UtilisateurAudio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "id_utilisateur")
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "audio_id")
    private Contenu contenu;

    @Column(name = "date_lecture")
    private LocalDateTime dateLecture;

    // Default constructor
    public UtilisateurAudio() {
    }

    // Constructor with all fields
    public UtilisateurAudio(Long id, Utilisateur utilisateur, Contenu contenu, LocalDateTime dateLecture) {
        this.id = id;
        this.utilisateur = utilisateur;
        this.contenu = contenu;
        this.dateLecture = dateLecture;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Contenu getContenu() {
        return contenu;
    }

    public void setContenu(Contenu contenu) {
        this.contenu = contenu;
    }

    public LocalDateTime getDateLecture() {
        return dateLecture;
    }

    public void setDateLecture(LocalDateTime dateLecture) {
        this.dateLecture = dateLecture;
    }

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UtilisateurAudio)) return false;
        UtilisateurAudio that = (UtilisateurAudio) o;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "UtilisateurAudio{" +
                "id=" + id +
                ", utilisateur=" + utilisateur +
                ", contenu=" + contenu +
                ", dateLecture=" + dateLecture +
                '}';
    }
}