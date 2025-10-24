package com.mussodeme.MussoDeme.entities;

import com.mussodeme.MussoDeme.enums.TypeProduit;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
@Table(name = "produit")
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est requis")
    private String nom;

    private String description;

    @NotBlank(message = "L'image est requis")
    private String image;

    @NotNull(message = "La quantite est requis")
    private Integer quantite;

    @NotNull(message = "Le prix est requis")
    private Double prix;
    
    /**
     * Type de produit pour la recherche vocale
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type_produit")
    private TypeProduit typeProduit;
    
    /**
     * URL du guide audio vocal pour ce produit
     * Permet aux femmes rurales d'écouter la description du produit
     * Ex: "Ce savon artisanal est fait avec du beurre de karité..."
     */
    @Column(name = "audio_guide_url")
    private String audioGuideUrl;

    @ManyToOne
    @JoinColumn(name = "femmeRural_id")
    private FemmeRurale femmeRurale;

    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL)
    private List<Commande> commandes;

    // Default constructor
    public Produit() {
    }

    // Constructor with all fields
    public Produit(Long id, String nom, String description, String image, Integer quantite, Double prix, 
                  TypeProduit typeProduit, String audioGuideUrl, FemmeRurale femmeRurale, List<Commande> commandes) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.image = image;
        this.quantite = quantite;
        this.prix = prix;
        this.typeProduit = typeProduit;
        this.audioGuideUrl = audioGuideUrl;
        this.femmeRurale = femmeRurale;
        this.commandes = commandes;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotBlank(message = "Le nom est requis")
    public String getNom() {
        return nom;
    }

    public void setNom(@NotBlank(message = "Le nom est requis") String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NotBlank(message = "L'image est requis")
    public String getImage() {
        return image;
    }

    public void setImage(@NotBlank(message = "L'image est requis") String image) {
        this.image = image;
    }

    @NotNull(message = "La quantite est requis")
    public Integer getQuantite() {
        return quantite;
    }

    // Adding getStock() method as an alias for getQuantite()
    public Integer getStock() {
        return quantite;
    }

    public void setQuantite(@NotNull(message = "La quantite est requis") Integer quantite) {
        this.quantite = quantite;
    }

    @NotNull(message = "Le prix est requis")
    public Double getPrix() {
        return prix;
    }

    public void setPrix(@NotNull(message = "Le prix est requis") Double prix) {
        this.prix = prix;
    }

    // Adding getVendeur() method as an alias for getFemmeRurale()
    public FemmeRurale getVendeur() {
        return femmeRurale;
    }

    public TypeProduit getTypeProduit() {
        return typeProduit;
    }

    public void setTypeProduit(TypeProduit typeProduit) {
        this.typeProduit = typeProduit;
    }

    public String getAudioGuideUrl() {
        return audioGuideUrl;
    }

    public void setAudioGuideUrl(String audioGuideUrl) {
        this.audioGuideUrl = audioGuideUrl;
    }

    public FemmeRurale getFemmeRurale() {
        return femmeRurale;
    }

    public void setFemmeRurale(FemmeRurale femmeRurale) {
        this.femmeRurale = femmeRurale;
    }

    public List<Commande> getCommandes() {
        return commandes;
    }

    public void setCommandes(List<Commande> commandes) {
        this.commandes = commandes;
    }

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Produit)) return false;
        Produit produit = (Produit) o;
        return id != null && id.equals(produit.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Produit{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", quantite=" + quantite +
                ", prix=" + prix +
                ", typeProduit=" + typeProduit +
                ", audioGuideUrl='" + audioGuideUrl + '\'' +
                ", femmeRurale=" + femmeRurale +
                '}';
    }
}