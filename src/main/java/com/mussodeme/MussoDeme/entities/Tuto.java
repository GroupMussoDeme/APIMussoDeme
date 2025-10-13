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
@Table(name = "tutos")

public class Tuto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String langue;
    private String description;
    private String videoUrl;
    private String duree;

    @ManyToOne
    @JoinColumn(nullable = false, name = "id_admin")
    private Admin admin;
}
