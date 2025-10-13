package com.mussodeme.MussoDeme.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "femmes_rurales")

public class FemmeRurale extends Utilisateur {

    @NotBlank(message = "Le Prenom est requis")
    private String prenom;

    @OneToMany(mappedBy = "femmes")
    private List<Produit> produits;

    @OneToMany(mappedBy = "membres")
    private List<Appartenance> appartenance = new ArrayList<>();

    @OneToMany(mappedBy = "femmes")
    private List<RechercherParLocalisation>  rechhercherParLocalisation = new ArrayList<>();

}
