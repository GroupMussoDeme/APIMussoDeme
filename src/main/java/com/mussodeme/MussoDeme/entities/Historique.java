package com.mussodeme.MussoDeme.entities;

import com.mussodeme.MussoDeme.enums.TypeHistoriques;
import com.mussodeme.MussoDeme.enums.TypeNotif;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "historiques")

public class Historique {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TypeHistoriques typeHistoriques;

    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "id_utilisateur")
    private Utilisateur utilisateur;
}
