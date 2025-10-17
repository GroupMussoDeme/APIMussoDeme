package com.mussodeme.MussoDeme.entities;

import com.mussodeme.MussoDeme.enums.StatutCommande;
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
@Table(name = "commande")

public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantite;

    @Enumerated(EnumType.STRING)
    private StatutCommande statutCommande;

    private LocalDateTime dateAchat;

    @ManyToOne
    @JoinColumn(name = "acheteur_id", nullable = false)
    private Acheteur acheteur;

    @ManyToOne
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    private List<Paiement> paiement;

}
