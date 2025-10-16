package com.mussodeme.MussoDeme.entities;

import jakarta.persistence.*;
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
@Data
@SuperBuilder
@DiscriminatorValue("ADMIN")


public class Admin extends Utilisateur {

    @Column(unique = true, nullable = true)
    private String email;

    @Column(nullable = true)
    private String motDePasse;

    @OneToMany(mappedBy = "admin")
    private List<AudioConseil> audiosConseils;

    @OneToMany(mappedBy = "admin")
    private List<Categorie> categories;

    @OneToMany(mappedBy = "admin")
    private List<Historique> historiques;

    @OneToMany(mappedBy = "admin")
    private List<Tuto>  tutos;

    @OneToMany(mappedBy = "admin")
    private List<GestionAdmin> gestionsAdmin = new ArrayList<>();
}
