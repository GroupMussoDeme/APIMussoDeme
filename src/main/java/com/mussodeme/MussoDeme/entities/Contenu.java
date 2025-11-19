package com.mussodeme.MussoDeme.entities;

import com.mussodeme.MussoDeme.enums.TypeInfo;
import com.mussodeme.MussoDeme.enums.TypeCategorie;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contenu")
public class Contenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String description;
    private String urlContenu;
    private String duree;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type_info")
    private TypeInfo typeInfo;

    @Enumerated(EnumType.STRING)
    @Column(name = "categorie")
    private TypeCategorie categorie;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @OneToMany(mappedBy = "contenu")
    private List<UtilisateurAudio> utilisateurAudio = new ArrayList<>();

    // Default constructor
    public Contenu() {
        this.utilisateurAudio = new ArrayList<>();
    }

    // Constructor with all fields
    public Contenu(Long id, String titre, String description, String urlContenu, String duree, 
                  TypeInfo typeInfo, TypeCategorie categorie, Admin admin, List<UtilisateurAudio> utilisateurAudio) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.urlContenu = urlContenu;
        this.duree = duree;
        this.typeInfo = typeInfo;
        this.categorie = categorie;
        this.admin = admin;
        this.utilisateurAudio = utilisateurAudio != null ? utilisateurAudio : new ArrayList<>();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlContenu() {
        return urlContenu;
    }

    public void setUrlContenu(String urlContenu) {
        this.urlContenu = urlContenu;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public TypeInfo getTypeInfo() {
        return typeInfo;
    }

    public void setTypeInfo(TypeInfo typeInfo) {
        this.typeInfo = typeInfo;
    }

    // Adding missing getTypeContenu() method as an alias for getTypeInfo()
    public TypeInfo getTypeContenu() {
        return typeInfo;
    }

    public TypeCategorie getCategorie() {
        return categorie;
    }

    public void setCategorie(TypeCategorie categorie) {
        this.categorie = categorie;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public List<UtilisateurAudio> getUtilisateurAudio() {
        return utilisateurAudio;
    }

    public void setUtilisateurAudio(List<UtilisateurAudio> utilisateurAudio) {
        this.utilisateurAudio = utilisateurAudio;
    }

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contenu)) return false;
        Contenu contenu = (Contenu) o;
        return id != null && id.equals(contenu.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Contenu{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", urlContenu='" + urlContenu + '\'' +
                ", duree='" + duree + '\'' +
                ", typeInfo=" + typeInfo +
                ", categorie=" + categorie +
                ", admin=" + admin +
                '}';
    }
}