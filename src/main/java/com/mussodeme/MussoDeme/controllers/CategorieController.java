package com.mussodeme.MussoDeme.controllers;

import com.mussodeme.MussoDeme.dto.CategorieDTO;
import com.mussodeme.MussoDeme.dto.Response;
import com.mussodeme.MussoDeme.services.CategorieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategorieController {

    private final CategorieService categorieService;

    // ------------------ CREATE ------------------
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategorieDTO> create(@RequestParam Long adminId,
                                               @RequestBody CategorieDTO dto) {
        return ResponseEntity.ok(categorieService.createCategorie(adminId, dto));
    }

    // ------------------ LIST ------------------
    @GetMapping("/list")
    public ResponseEntity<List<CategorieDTO>> list() {
        return ResponseEntity.ok(categorieService.listCategories());
    }

    // ------------------ GET BY ID ------------------
    @GetMapping("/{id}")
    public ResponseEntity<CategorieDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(categorieService.getCategorie(id));
    }

    // ------------------ UPDATE ------------------
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategorieDTO> update(@PathVariable Long id,
                                               @RequestBody CategorieDTO dto) {
        return ResponseEntity.ok(categorieService.updateCategorie(id, dto));
    }

    // ------------------ DELETE ------------------
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> delete(@PathVariable Long id) {
        categorieService.deleteCategorie(id);
        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Catégorie supprimée avec succès")
                .build());
    }
}
