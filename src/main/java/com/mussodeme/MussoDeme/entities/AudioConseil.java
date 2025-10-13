package com.mussodeme.MussoDeme.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "audioConseil")

public class AudioConseil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String langue;
    private String description;
    private String imageUrl;
    private String duree;

    @ManyToOne
    @JoinColumn(name = "categorie_id", nullable = false)
    private Categorie categorie;

    @ManyToOne
    @JoinColumn(name = "id_admin")
    private Admin admin;

    @OneToMany(mappedBy = "audio")
    private List<UtilisateurAudio> utilisateurAudio = new ArrayList<>();
}
