package com.mussodeme.MussoDeme.services;

import com.mussodeme.MussoDeme.dto.CategorieDTO;
import com.mussodeme.MussoDeme.entities.Admin;
import com.mussodeme.MussoDeme.entities.Categorie;
import com.mussodeme.MussoDeme.exceptions.NotFoundException;
import com.mussodeme.MussoDeme.repository.AdminRepository;
import com.mussodeme.MussoDeme.repository.CategorieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategorieService {

    private final CategorieRepository categorieRepo;
    private final AdminRepository adminRepo;

    // ------------------ CREATE ------------------
    public CategorieDTO createCategorie(Long adminId, CategorieDTO dto) {
        Admin admin = adminRepo.findById(adminId)
                .orElseThrow(() -> new NotFoundException("Admin introuvable"));

        Categorie cat = Categorie.builder()
                .typeCategorie(dto.getTypeCategorie())
                .admin(admin)
                .build();

        Categorie saved = categorieRepo.save(cat);
        return toDTO(saved);
    }

    // ------------------ LIST ------------------
    public List<CategorieDTO> listCategories() {
        return categorieRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    // ------------------ GET BY ID ------------------
    public CategorieDTO getCategorie(Long id) {
        Categorie cat = categorieRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Catégorie introuvable"));
        return toDTO(cat);
    }

    // ------------------ UPDATE ------------------
    public CategorieDTO updateCategorie(Long id, CategorieDTO dto) {
        Categorie cat = categorieRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Catégorie introuvable"));

        cat.setTypeCategorie(dto.getTypeCategorie());

        Categorie updated = categorieRepo.save(cat);
        return toDTO(updated);
    }

    // ------------------ DELETE ------------------
    public void deleteCategorie(Long id) {
        Categorie cat = categorieRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Catégorie introuvable"));
        categorieRepo.delete(cat);
    }

    // ------------------ Mapper ------------------
    private CategorieDTO toDTO(Categorie cat) {
        CategorieDTO dto = new CategorieDTO();
        dto.setId(cat.getId());
        dto.setTypeCategorie(cat.getTypeCategorie());
        return dto;
    }
}
