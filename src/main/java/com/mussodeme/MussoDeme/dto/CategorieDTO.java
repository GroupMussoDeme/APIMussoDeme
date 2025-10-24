package com.mussodeme.MussoDeme.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mussodeme.MussoDeme.enums.TypeCategorie;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategorieDTO {
    private Long id;
    private TypeCategorie typeCategorie;

    // Default constructor
    public CategorieDTO() {
    }

    // Constructor with all fields
    public CategorieDTO(Long id, TypeCategorie typeCategorie) {
        this.id = id;
        this.typeCategorie = typeCategorie;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeCategorie getTypeCategorie() {
        return typeCategorie;
    }

    public void setTypeCategorie(TypeCategorie typeCategorie) {
        this.typeCategorie = typeCategorie;
    }

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategorieDTO)) return false;
        CategorieDTO that = (CategorieDTO) o;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "CategorieDTO{" +
                "id=" + id +
                ", typeCategorie=" + typeCategorie +
                '}';
    }
}