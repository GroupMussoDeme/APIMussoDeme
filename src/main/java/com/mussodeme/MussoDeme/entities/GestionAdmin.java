package com.mussodeme.MussoDeme.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "gestion_admin")

public class GestionAdmin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "id_admin")
    private Admin admin;

    @ManyToOne
    @JoinColumn(nullable = false, name = "id_utilisateur")
    private Utilisateur utilisateur;

}
