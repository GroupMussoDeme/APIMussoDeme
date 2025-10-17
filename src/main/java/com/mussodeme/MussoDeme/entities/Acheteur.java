package com.mussodeme.MussoDeme.entities;

import com.mussodeme.MussoDeme.entities.Commande;
import com.mussodeme.MussoDeme.entities.Paiement;
import com.mussodeme.MussoDeme.entities.RechercherParLocalisation;
import com.mussodeme.MussoDeme.entities.Utilisateur;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DiscriminatorValue("ACHETEUR")
public class Acheteur extends Utilisateur {

    @OneToMany(mappedBy = "acheteur", cascade = CascadeType.ALL)
    private List<Commande> commandes;

    @OneToMany(mappedBy = "acheteur")
    private List<Paiement> paiements;

    @OneToMany(mappedBy = "acheteur")
    private List<RechercherParLocalisation> rechhercherParLocalisation = new ArrayList<>();
}
