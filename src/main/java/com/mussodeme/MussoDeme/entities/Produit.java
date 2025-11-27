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
    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotBlank(message = "L'image est requis")
    @Column(name = "image", nullable = false)
    private String image;

    @NotNull(message = "La quantite est requis")
    @Column(name = "quantite", nullable = false)
    private Integer quantite;

    @NotNull(message = "Le prix est requis")
    @Column(name = "prix", nullable = false)
    private Double prix;

    @Column(name = "unite", length = 30)
    private String unite;

    /**
     * Type de produit pour la recherche vocale
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type_produit")
    private TypeProduit typeProduit;

    /**
     * URL du guide audio vocal pour ce produit
     */
    @Column(name = "audio_guide_url")
    private String audioGuideUrl;

    @ManyToOne
    @JoinColumn(name = "femmeRural_id")
    private FemmeRurale femmeRurale;

    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL)
    private List<Commande> commandes;

    // ===================== CONSTRUCTEURS =====================

    public Produit() {
    }

    public Produit(Long id,
                   String nom,
                   String description,
                   String image,
                   Integer quantite,
                   Double prix,
                   String unite,
                   TypeProduit typeProduit,
                   String audioGuideUrl,
                   FemmeRurale femmeRurale,
                   List<Commande> commandes) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.image = image;
        this.quantite = quantite;
        this.prix = prix;
        this.unite = unite;
        this.typeProduit = typeProduit;
        this.audioGuideUrl = audioGuideUrl;
        this.femmeRurale = femmeRurale;
        this.commandes = commandes;
    }

    // ===================== GETTERS / SETTERS =====================

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    // Alias pratique
    public Integer getStock() {
        return quantite;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    // Alias pratique pour vendeur
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

    // ===================== equals / hashCode / toString =====================

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
                ", femmeRurale=" + (femmeRurale != null ? femmeRurale.getId() : null) +
                '}';
    }
}
