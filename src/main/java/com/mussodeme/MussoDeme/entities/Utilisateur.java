package com.mussodeme.MussoDeme.entities;

import com.mussodeme.MussoDeme.enums.Role;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
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
    private String email; // Adding email field

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
    
    @OneToMany(mappedBy = "acheteur")
    private List<Commande> commandes = new ArrayList<>();
    
    @OneToMany(mappedBy = "acheteur")
    private List<Paiement> paiements = new ArrayList<>();

    // Default constructor
    public Utilisateur() {
        this.notifications = new ArrayList<>();
        this.gestionsAdmin = new ArrayList<>();
        this.utilisateurAudio = new ArrayList<>();
        this.historiques = new ArrayList<>();
        this.commandes = new ArrayList<>();
        this.paiements = new ArrayList<>();
    }

    // Constructor with all fields
    public Utilisateur(Long id, String nom, String prenom, String numeroTel, String motCle, String localite, 
                      String email, Role role, boolean active, List<Notification> notifications, List<GestionAdmin> gestionsAdmin, 
                      List<UtilisateurAudio> utilisateurAudio, List<Historique> historiques, List<Commande> commandes, 
                      List<Paiement> paiements) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.numeroTel = numeroTel;
        this.motCle = motCle;
        this.localite = localite;
        this.email = email; // Adding email to constructor
        this.role = role;
        this.active = active;
        this.notifications = notifications != null ? notifications : new ArrayList<>();
        this.gestionsAdmin = gestionsAdmin != null ? gestionsAdmin : new ArrayList<>();
        this.utilisateurAudio = utilisateurAudio != null ? utilisateurAudio : new ArrayList<>();
        this.historiques = historiques != null ? historiques : new ArrayList<>();
        this.commandes = commandes != null ? commandes : new ArrayList<>();
        this.paiements = paiements != null ? paiements : new ArrayList<>();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNumeroTel() {
        return numeroTel;
    }

    public void setNumeroTel(String numeroTel) {
        this.numeroTel = numeroTel;
    }

    public String getMotCle() {
        return motCle;
    }

    public void setMotCle(String motCle) {
        this.motCle = motCle;
    }

    public String getLocalite() {
        return localite;
    }

    public void setLocalite(String localite) {
        this.localite = localite;
    }

    // Adding getEmail() method
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public List<GestionAdmin> getGestionsAdmin() {
        return gestionsAdmin;
    }

    public void setGestionsAdmin(List<GestionAdmin> gestionsAdmin) {
        this.gestionsAdmin = gestionsAdmin;
    }

    public List<UtilisateurAudio> getUtilisateurAudio() {
        return utilisateurAudio;
    }

    public void setUtilisateurAudio(List<UtilisateurAudio> utilisateurAudio) {
        this.utilisateurAudio = utilisateurAudio;
    }

    public List<Historique> getHistoriques() {
        return historiques;
    }

    public void setHistoriques(List<Historique> historiques) {
        this.historiques = historiques;
    }

    public List<Commande> getCommandes() {
        return commandes;
    }

    public void setCommandes(List<Commande> commandes) {
        this.commandes = commandes;
    }

    public List<Paiement> getPaiements() {
        return paiements;
    }

    public void setPaiements(List<Paiement> paiements) {
        this.paiements = paiements;
    }

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilisateur)) return false;
        Utilisateur that = (Utilisateur) o;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", numeroTel='" + numeroTel + '\'' +
                ", motCle='" + motCle + '\'' +
                ", localite='" + localite + '\'' +
                ", email='" + email + '\'' + // Adding email to toString
                ", role=" + role +
                ", active=" + active +
                ", createdAt=" + createdAt +
                '}';
    }
}