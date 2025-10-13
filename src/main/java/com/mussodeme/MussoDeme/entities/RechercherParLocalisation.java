package com.mussodeme.MussoDeme.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "rechercher_par_localisation")

public class RechercherParLocalisation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "id_acheteur")
    private Acheteur acheteur;

    @ManyToOne
    @JoinColumn(nullable = false, name = "femme_id")
    private FemmeRurale femmeRurale;
}
