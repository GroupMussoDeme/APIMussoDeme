package com.mussodeme.MussoDeme.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "acheteur")

public class Acheteur extends Utilisateur {
    @OneToMany(mappedBy = "acheteur")
    private List<Commande> commandes;

    @OneToMany(mappedBy = "acheteur")
    private List<Paiement>  paiements;

    @OneToMany(mappedBy = "acheteur")
    private List<RechercherParLocalisation> rechhercherParLocalisation = new ArrayList<>();
}
