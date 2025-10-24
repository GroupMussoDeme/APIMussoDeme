package com.mussodeme.MussoDeme.entities;

import com.mussodeme.MussoDeme.enums.TypeHistoriques;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Entité pour tracer toutes les actions importantes dans l'application
 * Permet de suivre les ventes, achats, téléchargements et paiements
 */
@Entity
@Table(name = "historiques")
public class Historique {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Type d'historique (VENTES, ACHATS, TELECHARGEMENTS, PAIEMENTS)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type_historique", nullable = false)
    private TypeHistoriques typeHistoriques;

    /**
     * Date et heure de l'action
     */
    @Column(name = "date_action", nullable = false)
    private LocalDateTime dateAction = LocalDateTime.now();

    /**
     * Utilisateur qui a effectué l'action
     */
    @ManyToOne
    @JoinColumn(name = "id_utilisateur", nullable = false)
    private Utilisateur utilisateur;
    
    /**
     * ID de l'entité concernée (produit, commande, contenu, paiement)
     */
    @Column(name = "entite_id")
    private Long entiteId;
    
    /**
     * Type de l'entité concernée (Produit, Commande, Contenu, Paiement)
     */
    @Column(name = "entite_type")
    private String entiteType;
    
    /**
     * Description détaillée de l'action (ex: "Vente de Savon artisanal - 5000 FCFA")
     */
    @Column(name = "description", length = 500)
    private String description;
    
    /**
     * Montant concerné (pour ventes, achats, paiements)
     */
    @Column(name = "montant")
    private Double montant;

    // Default constructor
    public Historique() {
    }

    // Constructor with all fields
    public Historique(Long id, TypeHistoriques typeHistoriques, LocalDateTime dateAction, Utilisateur utilisateur, 
                     Long entiteId, String entiteType, String description, Double montant) {
        this.id = id;
        this.typeHistoriques = typeHistoriques;
        this.dateAction = dateAction != null ? dateAction : LocalDateTime.now();
        this.utilisateur = utilisateur;
        this.entiteId = entiteId;
        this.entiteType = entiteType;
        this.description = description;
        this.montant = montant;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeHistoriques getTypeHistoriques() {
        return typeHistoriques;
    }

    public void setTypeHistoriques(TypeHistoriques typeHistoriques) {
        this.typeHistoriques = typeHistoriques;
    }

    public LocalDateTime getDateAction() {
        return dateAction;
    }

    public void setDateAction(LocalDateTime dateAction) {
        this.dateAction = dateAction;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Long getEntiteId() {
        return entiteId;
    }

    public void setEntiteId(Long entiteId) {
        this.entiteId = entiteId;
    }

    public String getEntiteType() {
        return entiteType;
    }

    public void setEntiteType(String entiteType) {
        this.entiteType = entiteType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Historique)) return false;
        Historique that = (Historique) o;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Historique{" +
                "id=" + id +
                ", typeHistoriques=" + typeHistoriques +
                ", dateAction=" + dateAction +
                ", utilisateur=" + utilisateur +
                ", entiteId=" + entiteId +
                ", entiteType='" + entiteType + '\'' +
                ", description='" + description + '\'' +
                ", montant=" + montant +
                '}';
    }
}