package com.mussodeme.MussoDeme.controllers;

import com.mussodeme.MussoDeme.dto.InstitutionFinanciereDTO;
import com.mussodeme.MussoDeme.dto.Response;
import com.mussodeme.MussoDeme.services.InstitutionFinanciereService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/institutions")
public class InstitutionFinanciereController {

    private final InstitutionFinanciereService service;

    public InstitutionFinanciereController(InstitutionFinanciereService service) {
        this.service = service;
    }

    // LISTE PUBLIQUE
    @GetMapping
    public ResponseEntity<List<InstitutionFinanciereDTO>> list() {
        return ResponseEntity.ok(service.getAll());
    }

    // GET PAR ID
    @GetMapping("/{id}")
    public ResponseEntity<InstitutionFinanciereDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    // ✅ CREATE AVEC IMAGE (ADMIN)
    @PostMapping(consumes = "multipart/form-data")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InstitutionFinanciereDTO> create(
            @RequestPart("data") InstitutionFinanciereDTO dto,
            @RequestPart(value = "logo", required = false) MultipartFile logo
    ) {
        return ResponseEntity.ok(service.create(dto, logo));
    }

    // ✅ UPDATE SANS IMAGE
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InstitutionFinanciereDTO> update(
            @PathVariable Long id,
            @RequestBody InstitutionFinanciereDTO dto
    ) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(
                Response.builder()
                        .status(200)
                        .message("Institution supprimée avec succès")
                        .build()
        );
    }
}
