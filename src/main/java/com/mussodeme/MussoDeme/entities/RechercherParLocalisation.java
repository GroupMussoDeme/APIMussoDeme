package com.mussodeme.MussoDeme.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "rechercher_par_localisation")
public class RechercherParLocalisation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "id_acheteur")
    private Acheteur acheteur;

    @ManyToOne
    @JoinColumn(nullable = false, name = "femmeRural_id")
    private FemmeRurale femmeRurale;
    
    private String localite;
    
    @Column(name = "date_recherche")
    private LocalDateTime dateRecherche;

    // Default constructor
    public RechercherParLocalisation() {
    }

    // Constructor with all fields
    public RechercherParLocalisation(Long id, Acheteur acheteur, FemmeRurale femmeRurale, String localite, LocalDateTime dateRecherche) {
        this.id = id;
        this.acheteur = acheteur;
        this.femmeRurale = femmeRurale;
        this.localite = localite;
        this.dateRecherche = dateRecherche;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Acheteur getAcheteur() {
        return acheteur;
    }

    public void setAcheteur(Acheteur acheteur) {
        this.acheteur = acheteur;
    }

    public FemmeRurale getFemmeRurale() {
        return femmeRurale;
    }

    public void setFemmeRurale(FemmeRurale femmeRurale) {
        this.femmeRurale = femmeRurale;
    }
    
    public String getLocalite() {
        return localite;
    }
    
    public void setLocalite(String localite) {
        this.localite = localite;
    }
    
    public LocalDateTime getDateRecherche() {
        return dateRecherche;
    }
    
    public void setDateRecherche(LocalDateTime dateRecherche) {
        this.dateRecherche = dateRecherche;
    }

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RechercherParLocalisation)) return false;
        RechercherParLocalisation that = (RechercherParLocalisation) o;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "RechercherParLocalisation{" +
                "id=" + id +
                ", acheteur=" + acheteur +
                ", femmeRurale=" + femmeRurale +
                ", localite='" + localite + '\'' +
                ", dateRecherche=" + dateRecherche +
                '}';
    }
}