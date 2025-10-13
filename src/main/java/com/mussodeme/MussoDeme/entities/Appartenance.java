package com.mussodeme.MussoDeme.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "appartenance")

public class Appartenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateIntegration;

    @ManyToOne
    @JoinColumn(nullable = false, name = "id_coperative")
    private Coperative coperative;

    @ManyToOne
    @JoinColumn(nullable = false, name = "femme_id")
    private FemmeRurale femmeRurale;
}
