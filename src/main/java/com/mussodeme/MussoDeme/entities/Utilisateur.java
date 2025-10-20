package com.mussodeme.MussoDeme.entities;

import com.mussodeme.MussoDeme.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "utilisateurs")
public abstract class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;
    private String numeroTel;
    private String motCle;
    private String localite;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean active;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private final LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "utilisateur")
    private List<Notification> notifications;

    @OneToMany(mappedBy = "utilisateur")
    private List<GestionAdmin> gestionsAdmin = new ArrayList<>();

    @OneToMany(mappedBy = "utilisateur")
    private List<UtilisateurAudio> utilisateurAudio = new ArrayList<>();

    @OneToMany(mappedBy = "utilisateur")
    private List<Historique> historiques;
}
