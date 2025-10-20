package com.mussodeme.MussoDeme.entities;

import com.mussodeme.MussoDeme.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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
@Table(name = "admin")

public class Admin  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String motDePasse;

    private boolean active;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "admin")
    private List<Contenu> contenus;

    @OneToMany(mappedBy = "admin")
    private List<Categorie> categories;

    @OneToMany(mappedBy = "admin")
    private List<Historique> historiques;

    @OneToMany(mappedBy = "admin")
    private List<GestionAdmin> gestionsAdmin = new ArrayList<>();
}
