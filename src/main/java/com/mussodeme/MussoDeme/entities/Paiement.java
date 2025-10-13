package com.mussodeme.MussoDeme.entities;

import com.mussodeme.MussoDeme.enums.ModePaiement;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "paiement")

public class Paiement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private LocalDateTime datePaiement;

    @Enumerated(EnumType.STRING)
    private ModePaiement modePaiement;

    private LocalDateTime datesPaiement;

    private Double montant;

    @OneToMany(mappedBy = "paiement")
    private List<Commande> commande;

    @ManyToOne
    @JoinColumn(name = "id_acheteur")
    private Acheteur acheteur;

}
