package com.mussodeme.MussoDeme.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "produit")

public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est requis")
    private String nom;

    private String description;

    @NotBlank(message = "L'image est requis")
    private String image;

    @NotNull(message = "La quantite est requis")
    private Integer quantite;


    @NotNull(message = "Le prix est requis")
    private Double prix;

    @ManyToOne
    @JoinColumn(name = "femme_id")
    private FemmeRurale femmeRurale;

    @OneToMany(mappedBy = "produit")
    private List<Commande> commandes;

}
