package com.mussodeme.MussoDeme.controllers;

import com.mussodeme.MussoDeme.dto.ContenuDTO;
import com.mussodeme.MussoDeme.dto.Response;
import com.mussodeme.MussoDeme.services.ContenuService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/contenus")
public class ContenuController {

    private final ContenuService contenuService;

    // Constructor for dependency injection
    public ContenuController(ContenuService contenuService) {
        this.contenuService = contenuService;
    }

    // ------------------ CREER / UPLOADER ------------------
    @PostMapping(value = "/televerser", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ContenuDTO televerserContenu(
            @RequestParam("file") MultipartFile file,
            @RequestParam("titre") String titre,
            @RequestParam("description") String description,
            @RequestParam("duree") String duree,
            @RequestParam("typeInfo") String typeInfo,
            @RequestParam("categorie") String categorie,
            @RequestParam("adminId") Long adminId
    ) throws IOException {
        return contenuService.televerserContenu(file, titre, description, duree, typeInfo, categorie, adminId);
    }


    // ------------------ LIST ------------------
    @GetMapping("/list")
    public ResponseEntity<List<ContenuDTO>> list() {
        return ResponseEntity.ok(contenuService.listContenus());
    }

    // ------------------ GET BY ID ------------------
    @GetMapping("/{id}")
    public ResponseEntity<ContenuDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(contenuService.getContenu(id));
    }

    // ------------------ DOWNLOAD ------------------
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable Long id) throws IOException {
        Resource file = contenuService.downloadContenu(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    // ------------------ UPDATE ------------------
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ContenuDTO> update(@PathVariable Long id,
                                             @RequestBody ContenuDTO dto) {
        return ResponseEntity.ok(contenuService.updateContenu(id, dto));
    }

    // ------------------ DELETE ------------------
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> delete(@PathVariable Long id) throws IOException {
        contenuService.deleteContenu(id);
        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Contenu supprimé avec succès")
                .build());
    }
}