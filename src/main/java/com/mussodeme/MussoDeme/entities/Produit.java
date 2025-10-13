package com.mussodeme.MussoDeme.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "La quantite est requis")
    private Integer quantite;


    @NotBlank(message = "Le prix est requis")
    private Double prix;

    @ManyToOne
    @JoinColumn(name = "id_femme")
    private FemmeRurale femmeRurales;

    @OneToMany(mappedBy = "produit")
    private List<Commande> commandes;

}
