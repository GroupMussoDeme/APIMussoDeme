package com.mussodeme.MussoDeme.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@DiscriminatorValue("FEMME_RURALE")
public class FemmeRurale extends Utilisateur {

    @OneToMany(mappedBy = "femmeRurale")
    private List<Produit> produits;

    @OneToMany(mappedBy = "femmeRurale")
    private List<Appartenance> appartenance = new ArrayList<>();

    @OneToMany(mappedBy = "femmeRurale")
    private List<RechercherParLocalisation>  rechhercherParLocalisation = new ArrayList<>();

}
