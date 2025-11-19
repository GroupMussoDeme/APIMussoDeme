package com.mussodeme.MussoDeme.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mussodeme.MussoDeme.enums.TypeInfo;
import com.mussodeme.MussoDeme.dto.CategorieDTO;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContenuDTO {
    private Long id;
    private String titre;
    private String description;
    private String urlContenu;
    private String duree;
    private TypeInfo typeInfo;

    private Long adminId;
    private String typeCategorie;

    // Default constructor
    public ContenuDTO() {
    }

    // Constructor with all fields
    public ContenuDTO(Long id, String titre, String description, 
                      String urlContenu, String duree, TypeInfo typeInfo,
                      Long adminId, String typeCategorie) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.urlContenu = urlContenu;
        this.duree = duree;
        this.typeInfo = typeInfo;
        this.adminId = adminId;
        this.typeCategorie = typeCategorie;
    }
    
    // Ancien constructeur pour compatibilité avec AdminService
    public ContenuDTO(Long id, String titre, String description, 
                      String urlContenu, String duree, Long adminId, String typeCategorie) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.urlContenu = urlContenu;
        this.duree = duree;
        this.adminId = adminId;
        this.typeCategorie = typeCategorie;
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

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    

    public String getTypeCategorie() {
        return typeCategorie;
    }

    public void setTypeCategorie(String typeCategorie) {
        this.typeCategorie = typeCategorie;
    }

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContenuDTO)) return false;
        ContenuDTO that = (ContenuDTO) o;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "ContenuDTO{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", urlContenu='" + urlContenu + '\'' +
                ", duree='" + duree + '\'' +
                ", typeInfo=" + typeInfo +
                ", adminId=" + adminId +
                ", typeCategorie='" + typeCategorie + '\'' +
                '}';
    }
}