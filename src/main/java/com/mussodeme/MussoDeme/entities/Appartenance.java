package com.mussodeme.MussoDeme.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "appartenance")
public class Appartenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_adhesion")
    private LocalDateTime dateAdhesion;

    @ManyToOne
    @JoinColumn(nullable = false, name = "coperative_id")
    private Coperative coperative;

    @ManyToOne
    @JoinColumn(nullable = false, name = "femmeRurale_id")
    private FemmeRurale femmeRurale;

    // Default constructor
    public Appartenance() {
    }

    // Constructor with all fields
    public Appartenance(Long id, LocalDateTime dateAdhesion, Coperative coperative, FemmeRurale femmeRurale) {
        this.id = id;
        this.dateAdhesion = dateAdhesion;
        this.coperative = coperative;
        this.femmeRurale = femmeRurale;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateAdhesion() {
        return dateAdhesion;
    }

    public void setDateAdhesion(LocalDateTime dateAdhesion) {
        this.dateAdhesion = dateAdhesion;
    }

    public Coperative getCoperative() {
        return coperative;
    }

    public void setCoperative(Coperative coperative) {
        this.coperative = coperative;
    }

    public FemmeRurale getFemmeRurale() {
        return femmeRurale;
    }

    public void setFemmeRurale(FemmeRurale femmeRurale) {
        this.femmeRurale = femmeRurale;
    }

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Appartenance)) return false;
        Appartenance that = (Appartenance) o;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Appartenance{" +
                "id=" + id +
                ", dateAdhesion=" + dateAdhesion +
                ", coperative=" + coperative +
                ", femmeRurale=" + femmeRurale +
                '}';
    }
}