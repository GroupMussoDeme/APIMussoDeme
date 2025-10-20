package com.mussodeme.MussoDeme.entities;

import com.mussodeme.MussoDeme.enums.TypeCategorie;
import jakarta.persistence.*;
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
@Table(name = "categorie")

public class Categorie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TypeCategorie typeCategorie;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;


    @OneToMany(mappedBy = "categorie", cascade = CascadeType.ALL)
    private List<Contenu> contenus;

}
