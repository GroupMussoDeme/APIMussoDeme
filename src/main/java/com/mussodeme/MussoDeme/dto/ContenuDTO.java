package com.mussodeme.MussoDeme.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mussodeme.MussoDeme.enums.TypeInfo;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContenuDTO {
    private Long id;
    private String titre;
    private String langue;
    private String description;
    private String urlContenu;
    private String duree;
    private TypeInfo typeInfo;

    private Long adminId;
    private Long categorieId;

    // Default constructor
    public ContenuDTO() {
    }

    // Constructor with all fields
    public ContenuDTO(Long id, String titre, String langue, String description, 
                      String urlContenu, String duree, Long adminId, Long categorieId) {
        this.id = id;
        this.titre = titre;
        this.langue = langue;
        this.description = description;
        this.urlContenu = urlContenu;
        this.duree = duree;
        this.adminId = adminId;
        this.categorieId = categorieId;
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

    public String getLangue() {
        return langue;
    }

    public void setLangue(String langue) {
        this.langue = langue;
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

    public Long getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(Long categorieId) {
        this.categorieId = categorieId;
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
                ", langue='" + langue + '\'' +
                ", description='" + description + '\'' +
                ", urlContenu='" + urlContenu + '\'' +
                ", duree='" + duree + '\'' +
                ", typeInfo=" + typeInfo +
                ", adminId=" + adminId +
                ", categorieId=" + categorieId +
                '}';
    }
}