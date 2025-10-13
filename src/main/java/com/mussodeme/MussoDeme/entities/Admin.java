package com.mussodeme.MussoDeme.entities;

import jakarta.persistence.Column;
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
@Table(name = "admin")

public class Admin extends Utilisateur {

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
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
